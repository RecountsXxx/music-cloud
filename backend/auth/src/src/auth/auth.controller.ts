import {
  Controller,
  Post,
  Body,
  UnauthorizedException,
  UseGuards,
  Request,
  Get,
} from '@nestjs/common';
import { AuthService } from './auth.service';
import { User } from './user.entity';
import { JwtAuthGuard } from './jwt-auth.guard';
import { UserDto } from './dtos/user.dto';
import { AuthResponse } from './responses/auth.response';

@Controller('auth')
export class AuthController {
  constructor(private readonly authService: AuthService) {}

  @Post('register')
  async register(
    @Body() createUserDto: { email: string; username: string; password: string; },
  ): Promise<AuthResponse> {
    return this.authService.register(
      createUserDto.email,
      createUserDto.username,
      createUserDto.password,
    );
  }

  @Post('login')
  async login(
    @Body() loginDto: { email: string; password: string },
  ): Promise<AuthResponse> {
    return this.authService.login(
      loginDto.email,
      loginDto.password
    );
  }

  @UseGuards(JwtAuthGuard)
  @Get('profile')
  getProfile(@Request() req): UserDto {
    return req.user;
  }
}
