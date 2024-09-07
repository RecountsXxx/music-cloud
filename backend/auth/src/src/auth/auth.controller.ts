import {
  Controller,
  Post,
  Body,
  UseGuards,
  Request,
  Response,
  Get,
} from '@nestjs/common';
//import { Request, Res } from 'express';
import { AuthService } from './auth.service';
import { JwtAuthGuard } from './jwt-auth.guard';
import { UserDto } from './dtos/user.dto';
import { AuthResponse } from './responses/auth.response';
import { UnauthorizedRequestException } from '../exceptions/unauthorized.request.exception';
import { RefreshTokenException } from '../exceptions/refresh.token.exception';

@Controller('auth')
export class AuthController {
  constructor(private readonly authService: AuthService) {}

  @Post('register')
  async register(
    @Body()
    createUserDto: {
      email: string;
      username: string;
      password: string;
    },
    @Response({ passthrough: true }) res,
  ): Promise<AuthResponse> {
    const { authResponse, refreshToken } = await this.authService.register(
      createUserDto.email,
      createUserDto.username,
      createUserDto.password,
    );

    res.cookie('refreshToken', refreshToken, {
      httpOnly: true,
      secure: false,
      sameSite: 'strict',
      path: '/',
    });

    return authResponse;
  }

  @Post('login')
  async login(
    @Body() loginDto: { email: string; password: string },
    @Response({ passthrough: true }) res,
  ): Promise<AuthResponse> {
    const { authResponse, refreshToken } = await this.authService.login(
      loginDto.email,
      loginDto.password,
    );

    res.cookie('refreshToken', refreshToken, {
      httpOnly: true,
      secure: false,
      sameSite: 'strict',
      path: '/',
    });

    return authResponse;
  }

  @Post('refresh-token')
  async refreshToken(
    @Request() req,
    @Response({ passthrough: true }) res,
  ): Promise<AuthResponse> {
    const refreshToken = req.cookies?.refreshToken;
    if (!refreshToken) {
      throw new RefreshTokenException({
        message: 'Refresh token not found',
        refreshTokenStatus: 'ERROR_REFRESH_TOKEN',
      });
    }

    const newTokens = await this.authService.refreshToken(refreshToken);

    res.cookie('refreshToken', newTokens.refreshToken, {
      httpOnly: true,
      secure: false,
      sameSite: 'strict',
      path: '/',
    });

    return newTokens.authResponse;
  }

  @UseGuards(JwtAuthGuard)
  @Get('me')
  getProfile(@Request() req): UserDto {
    return req.user;
  }
}
