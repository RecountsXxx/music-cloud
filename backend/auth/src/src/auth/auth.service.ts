import {Injectable} from '@nestjs/common';
import {InjectRepository} from '@nestjs/typeorm';
import {Repository} from 'typeorm';
import {JwtService} from '@nestjs/jwt';
import {User} from './user.entity';
import * as bcrypt from 'bcryptjs';
import {JwtPayload} from './jwt-payload.interface';
import {UserDto} from './dtos/user.dto';
import {AuthResponse} from './responses/auth.response';
import {InvalidCredentialsException} from '../exceptions/invalid-credentials.exception';

@Injectable()
export class AuthService {
    constructor(
        @InjectRepository(User)
        private usersRepository: Repository<User>,
        private jwtService: JwtService,
    ) {
    }

    async register(
        email: string,
        username: string,
        password: string,
    ): Promise<AuthResponse> {
        const hashedPassword = await bcrypt.hash(password, 10);
        const user = this.usersRepository.create({
            email,
            username,
            password: hashedPassword,
        });
        const savedUser = await this.usersRepository.save(user);
        const payload = {userId: savedUser.id};
        const access_token = this.jwtService.sign(payload);

        return new AuthResponse(UserDto.mapUser(savedUser), access_token);
    }

    async validateUser(email: string, password: string): Promise<User | null> {
        const user = await this.usersRepository.findOne({where: {email}});
        if (user && (await bcrypt.compare(password, user.password))) {
            return user;
        }
        return null;
    }

    async validateUserById(id: string): Promise<User | null> {
        return this.usersRepository.findOne({where: {id}});
    }

    async login(
        email: string,
        password: string
    ): Promise<AuthResponse> {
        const user = await this.validateUser(email, password);

        if (!user) {
            throw new InvalidCredentialsException();
        }

        const payload = {userId: user.id};
        const access_token = this.jwtService.sign(payload);

        return new AuthResponse(UserDto.mapUser(user), access_token);
    }

    async validateToken(
        token: string,
    ): Promise<{ valid: boolean; userDTO: UserDto | null }> {
        try {
            const decoded = this.jwtService.verify<JwtPayload>(token);
            const user = await this.usersRepository.findOne({
                where: {id: decoded.userId},
            });
            return {valid: !!user, userDTO: user ? UserDto.mapUser(user) : null};
        } catch (e) {
            return {valid: false, userDTO: null};
        }
    }
}
