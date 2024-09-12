import { join } from 'path';
import { Transport, ClientOptions } from '@nestjs/microservices';

export const authGrpcClientOptions: ClientOptions = {
  transport: Transport.GRPC,
  options: {
    url: 'api.auth:3010',
    package: 'auth',
    protoPath: join(__dirname, 'auth.proto'),
  },
};
