import { Controller } from '@nestjs/common';
import { GrpcMethod } from '@nestjs/microservices';
import { AuthService } from '../../auth/auth.service';
import { TokenValidationResponse } from '../../auth/responses/token.validation.response';

@Controller('grpc-auth')
export class AuthGrpcController {
  constructor(private readonly authService: AuthService) {}

  @GrpcMethod('AuthService', 'ValidateToken')
  async validateToken(data: {
    token: string;
  }): Promise<TokenValidationResponse> {
    return await this.authService.validateToken(data.token);
  }
}
