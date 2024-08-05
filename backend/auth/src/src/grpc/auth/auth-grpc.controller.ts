import { Controller } from '@nestjs/common';
import { GrpcMethod } from '@nestjs/microservices';
import { AuthService } from '../../auth/auth.service';
import { UserDto } from '../../auth/dtos/user.dto';

@Controller('grpc-auth')
export class AuthGrpcController {
  constructor(private readonly authService: AuthService) {}

  @GrpcMethod('AuthService', 'ValidateToken')
  async validateToken(data: { token: string }) : Promise<{ valid: boolean; userDTO: UserDto | null }> {
    const { valid, userDTO } = await this.authService.validateToken(data.token);
    return {
      valid,
      userDTO
    };
  }
}
