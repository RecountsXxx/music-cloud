import { Injectable, Logger } from '@nestjs/common';
import amqp from 'amqp-connection-manager';
import { ConfigService } from '@nestjs/config';

@Injectable()
export class RabbitMQService {
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
      this.channel = await this.connection.createChannel({
        json: true,
        setup: (channel) => {
          return channel.assertQueue('user.register', { durable: true });
        },
      });
      console.log('RabbitMQ Connection is Successful');
    } catch (error) {
      console.log('RabbitMQ Connection Failed: ', error);
    }
  }

  async sendMessage(queue: string, message: any) {
    this.logger.log(
      `Sending message to queue ${queue}: ${JSON.stringify(message)}`,
    );
    if (!this.channel) {
      await this.connect();
    }
    this.channel.sendToQueue(queue, Buffer.from(JSON.stringify(message)), {
      persistent: true,
    });
  }
}
