import { join } from 'path';
import { Transport, ClientOptions } from '@nestjs/microservices';

export const grpcClientOptions: ClientOptions = {
  transport: Transport.GRPC,
  options: {
    url: 'auth.api:3001',
    package: 'auth',
    protoPath: join(__dirname, 'auth.proto')
  }
};
