import { ClientOptions, Transport } from '@nestjs/microservices';
import { ConfigService } from '@nestjs/config';

export const rabbitmqOptions = (
  configService: ConfigService,
): ClientOptions => ({
  transport: Transport.RMQ,
  options: {
    urls: [configService.get<string>('RABBITMQ_URL')],
    queue: configService.get<string>('RABBITMQ_QUEUE', 'socket-messages'),
    queueOptions: {
      durable: true,
    },
  },
});
