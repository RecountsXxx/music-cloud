import { Injectable } from '@nestjs/common';
import { PassportStrategy } from '@nestjs/passport';
import { ExtractJwt, Strategy } from 'passport-jwt';
import { ConfigService } from '@nestjs/config';
import { AuthService } from './auth.service';
import { JwtPayload } from './jwt-payload.interface';
import { UserDto } from './dtos/user.dto';
import { UnauthorizedRequestException } from '../exceptions/unauthorized.request.exception';

@Injectable()
export class JwtStrategy extends PassportStrategy(Strategy) {
  constructor(
    private authService: AuthService,
    private configService: ConfigService,
  ) {
    super({
      jwtFromRequest: ExtractJwt.fromAuthHeaderAsBearerToken(),
      ignoreExpiration: false,
      secretOrKey: configService.get<string>('JWT_SECRET'),
    });
  }

  async validate(payload: JwtPayload): Promise<UserDto> {
    const user = await this.authService.validateUserById(payload.userId);
    if (!user) {
      throw new UnauthorizedRequestException();
    }
    return UserDto.mapUser(user);
  }
}
