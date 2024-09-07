import { Observable } from 'rxjs';
import { Injectable, OnModuleInit } from '@nestjs/common';
import {
  ClientGrpcProxy,
  ClientGrpc,
  Client,
  ClientProxyFactory,
} from '@nestjs/microservices';
import { authGrpcClientOptions } from './auth.grpc.options';
import { ConfigService } from '@nestjs/config';

export interface ValidateTokenRequest {
  token: string;
}

export interface UserDTO {
  id: string;
  email: string;
  username: string;
}

export interface ValidateTokenResponse {
  tokenStatus: string;
  userDTO: UserDTO;
}

export interface AuthService {
  validateToken(data: ValidateTokenRequest): Observable<ValidateTokenResponse>;
}

@Injectable()
export class AuthGrpcService implements OnModuleInit {
  private authService: AuthService;

  constructor(private readonly configService: ConfigService) {}

  onModuleInit() {
    const client: ClientGrpc = ClientProxyFactory.create(
      authGrpcClientOptions(this.configService),
    ) as ClientGrpcProxy;

    this.authService = client.getService<AuthService>('AuthService');
  }

  validateToken(data: ValidateTokenRequest): Observable<ValidateTokenResponse> {
    return this.authService.validateToken(data);
  }
}
