import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { JwtService } from '@nestjs/jwt';
import { User } from './entities/user.entity';
import * as bcrypt from 'bcryptjs';
import { JwtPayload } from './jwt-payload.interface';
import { UserDto } from './dtos/user.dto';
import { AuthResponse } from './responses/auth.response';
import { InvalidCredentialsException } from '../exceptions/invalid-credentials.exception';
import { RabbitMQService } from '../rabbitmq/rabbitmq.service';
import { firstValueFrom } from 'rxjs';
import { MediaGrpcService } from '../grpc/media/media.service';
import { EmailAlreadyExistsException } from '../exceptions/email-already-exists.exception';
import { UsernameAlreadyExistsException } from '../exceptions/username-already-exists-exception';
import { ConfigService } from '@nestjs/config';
import { UnauthorizedRequestException } from '../exceptions/unauthorized.request.exception';
import { TokenValidationResponse } from './responses/token.validation.response';
import { RefreshTokenException } from '../exceptions/refresh.token.exception';

@Injectable()
export class AuthService {
  constructor(
    @InjectRepository(User)
    private usersRepository: Repository<User>,
    private jwtService: JwtService,
    private rabbitMQService: RabbitMQService,
    private mediaGrpcService: MediaGrpcService,
    private readonly configService: ConfigService,
  ) {}

  private async generateRefreshToken(userId: string): Promise<string> {
    const payload = { userId };
    const refreshToken = this.jwtService.sign(payload, {
      secret: this.configService.get<string>('JWT_REFRESH_SECRET'),
      expiresIn: this.configService.get<string>('JWT_REFRESH_EXPIRES_IN'),
    });

    await this.saveRefreshToken(userId, refreshToken);
    return refreshToken;
  }

  // Метод для збереження `refresh token` в базі даних або кеші
  private async saveRefreshToken(
    userId: string,
    refreshToken: string,
  ): Promise<void> {
    const user = await this.validateUserById(userId);

    user.refreshToken = await bcrypt.hash(refreshToken, 10);
    await this.usersRepository.save(user);
  }

  async refreshToken(
    refreshToken: string,
  ): Promise<{ authResponse: AuthResponse; refreshToken: string }> {
    try {
      const decoded = this.jwtService.verify<JwtPayload>(refreshToken, {
        secret: this.configService.get<string>('JWT_REFRESH_SECRET'),
      });

      const user = await this.usersRepository.findOne({
        where: { id: decoded.userId },
      });

      if (!user) {
        throw new RefreshTokenException({
          message: 'User not found',
          refreshTokenStatus: 'ERROR_REFRESH_TOKEN',
        });
      }

      const newAccessToken = this.jwtService.sign({ userId: user.id });
      const newRefreshToken = await this.generateRefreshToken(user.id);

      const avatarsResponse = await firstValueFrom(
        this.mediaGrpcService.getAvatars(user.id),
      );

      const authResponse = new AuthResponse(
        UserDto.mapUser(user),
        newAccessToken,
        avatarsResponse.avatars,
      );

      return { authResponse, refreshToken: newRefreshToken };
    } catch (e) {
      throw new UnauthorizedRequestException('Invalid refresh token');
      throw new RefreshTokenException({
        message: 'Invalid refresh token',
        refreshTokenStatus: 'INVALID_REFRESH_TOKEN',
      });
    }
  }

  async register(
    email: string,
    username: string,
    password: string,
  ): Promise<{ authResponse: AuthResponse; refreshToken: string }> {
    const emailExists = await this.usersRepository.findOneBy({ email });
    if (emailExists) {
      throw new EmailAlreadyExistsException();
    }

    const usernameExists = await this.usersRepository.findOneBy({ username });
    if (usernameExists) {
      throw new UsernameAlreadyExistsException();
    }

    const hashedPassword = await bcrypt.hash(password, 10);
    const user = this.usersRepository.create({
      email,
      username,
      password: hashedPassword,
    });

    const savedUser = await this.usersRepository.save(user);
    const payload = { userId: savedUser.id };
    const accessToken = this.jwtService.sign(payload);

    const userDto = UserDto.mapUser(savedUser);

    await this.rabbitMQService.sendMessage('user.register', userDto);

    const avatarsResponse = await firstValueFrom(
      this.mediaGrpcService.getAvatars(userDto.id),
    );

    const refreshToken = await this.generateRefreshToken(userDto.id);

    const authResponse = new AuthResponse(
      userDto,
      accessToken,
      avatarsResponse.avatars,
    );

    return { authResponse, refreshToken };
  }

  async validateUser(email: string, password: string): Promise<User | null> {
    const user = await this.usersRepository.findOne({ where: { email } });
    if (user && (await bcrypt.compare(password, user.password))) {
      return user;
    }
    return null;
  }

  async validateUserById(id: string): Promise<User | null> {
    return this.usersRepository.findOne({ where: { id } });
  }

  async login(
    email: string,
    password: string,
  ): Promise<{ authResponse: AuthResponse; refreshToken: string }> {
    const user = await this.validateUser(email, password);

    if (!user) {
      throw new InvalidCredentialsException();
    }

    const payload = { userId: user.id };
    const accessToken = this.jwtService.sign(payload);

    const avatarsResponse = await firstValueFrom(
      this.mediaGrpcService.getAvatars(user.id),
    );

    const refreshToken = await this.generateRefreshToken(user.id);

    const authResponse = new AuthResponse(
      UserDto.mapUser(user),
      accessToken,
      avatarsResponse.avatars,
    );

    return { authResponse, refreshToken };
  }

  async validateToken(token: string): Promise<TokenValidationResponse> {
    try {
      const decoded = this.jwtService.verify<JwtPayload>(token);
      const user = await this.usersRepository.findOne({
        where: { id: decoded.userId },
      });

      if (!user) {
        return new TokenValidationResponse('INVALID_TOKEN', null);
      }

      return new TokenValidationResponse('VALID_TOKEN', UserDto.mapUser(user));
    } catch (e) {
      if (e.name === 'TokenExpiredError') {
        const decoded = this.jwtService.decode(token) as JwtPayload;
        const user = await this.usersRepository.findOne({
          where: { id: decoded.userId },
        });

        return new TokenValidationResponse(
          'EXPIRED_TOKEN',
          user ? UserDto.mapUser(user) : null,
        );
      }

      console.error(e);

      return new TokenValidationResponse('ERROR_TOKEN', null);
    }
  }
}
