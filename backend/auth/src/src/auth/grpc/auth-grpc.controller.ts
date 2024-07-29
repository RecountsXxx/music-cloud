import { Controller } from '@nestjs/common';
import { GrpcMethod } from '@nestjs/microservices';
import { AuthService } from '../auth.service';
import { UserDto } from '../dtos/user.dto';

@Controller()
export class AuthGrpcController {
  constructor(private readonly authService: AuthService) {}

  @GrpcMethod('AuthService', 'ValidateToken')
  async validateToken(data: { token: string }) : Promise<{ valid: boolean; user: UserDto | null }> {
    const { valid, user } = await this.authService.validateToken(data.token);
    return {
      valid,
      user
    };
  }
}
