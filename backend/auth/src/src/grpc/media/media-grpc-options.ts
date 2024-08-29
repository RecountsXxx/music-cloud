import { join } from 'path';
import { ClientOptions, Transport } from '@nestjs/microservices';

export const mediaGrpcClientOptions: ClientOptions = {
  transport: Transport.GRPC,
  options: {
    url: 'java.api:8090',
    package: 'media',
    protoPath: join(__dirname, 'media.proto'),
  },
};
