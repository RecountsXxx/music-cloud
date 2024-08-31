import { Module } from '@nestjs/common';
import { ConfigModule } from '@nestjs/config';
import { SocketGateway } from './gateways/socket.gateway';
import { RabbitMQListener } from './rabbitmq/rabbitmq.listener';
import { AuthGrpcService } from './auth/auth.service';

@Module({
  imports: [ConfigModule.forRoot()],
  providers: [AuthGrpcService, SocketGateway, RabbitMQListener],
})
export class AppModule {}
