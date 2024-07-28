import { join } from 'path';
import { Transport, ClientOptions } from '@nestjs/microservices';

export const grpcClientOptions: ClientOptions = {
  transport: Transport.GRPC,
  options: {
    package: 'auth',
    protoPath: join(__dirname, 'auth.proto'),
  }
};
