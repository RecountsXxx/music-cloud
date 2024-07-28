import { Controller } from '@nestjs/common';
import { GrpcMethod } from '@nestjs/microservices';
import { AuthService } from '../auth.service';

@Controller()
export class AuthGrpcController {
  constructor(private readonly authService: AuthService) {}

  @GrpcMethod('AuthService', 'ValidateToken')
  async validateToken(data: { token: string }) {
    const { valid, user } = await this.authService.validateToken(data.token);
    return {
      valid,
      user: user
        ? { id: user.id, email: user.email, username: user.username }
        : null,
    };
  }
}
