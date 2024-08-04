import { Module } from '@nestjs/common';
import { ConfigModule, ConfigService } from '@nestjs/config';
import { ClientsModule, Transport } from '@nestjs/microservices';
import { RabbitMQService } from './rabbitmq.service';

@Module({
  imports: [
    ConfigModule,
    ClientsModule.registerAsync([
      {
        name: 'RABBITMQ_SERVICE',
        imports: [ConfigModule],
        useFactory: (configService: ConfigService) => ({
          transport: Transport.RMQ,
          options: {
            urls: [configService.get<string>('RABBITMQ_URL')],
            queue: 'user.register',
            queueOptions: {
              durable: true,

            },
          },
        }),
        inject: [ConfigService]
      }
    ])
  ],
  providers: [RabbitMQService],
  exports: [RabbitMQService, ClientsModule]
})
export class RabbitMQModule {}
