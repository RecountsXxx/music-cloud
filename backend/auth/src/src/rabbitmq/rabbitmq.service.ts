import { Inject, Injectable, Logger } from '@nestjs/common';
import { ClientProxy } from '@nestjs/microservices';
import amqp from 'amqp-connection-manager';
import { ConfigService } from '@nestjs/config';

@Injectable()
export class RabbitMQService  {
  private readonly logger = new Logger(RabbitMQService.name);
  private connection;
  private channel;

  constructor(private readonly configService: ConfigService) {
    this.connect();
  }

  async connect() {
    try {
      const url = this.configService.get<string>('RABBITMQ_URL');
      this.connection = await amqp.connect(url);
      this.channel = await this.connection.createChannel();
      console.log('RABBITMQ CONNECTION IS SUCCESSFUL');
    } catch (e) {
      console.log('RABBITMQ CONNECTION HAVE FAILED', e);
    }
  }

  async sendMessage(queue: string, message: any) {
    this.logger.log(`Sending message to queue ${queue}: ${JSON.stringify(message)}`);
    if (!this.channel) {
      await this.connect();
    }
    this.channel.sendToQueue(queue, Buffer.from(JSON.stringify(message)), {
      persistent: true,
    });
  }
}