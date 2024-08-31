import { ClientOptions, Transport } from '@nestjs/microservices';
import { join } from 'path';
import { ConfigService } from '@nestjs/config';

export const authGrpcClientOptions = (
  configService: ConfigService,
): ClientOptions => ({
  transport: Transport.GRPC,
  options: {
    url: configService.get<string>('AUTH_GRPC_URL'),
    package: 'auth',
    protoPath: join(__dirname, 'auth.proto'),
  },
});
