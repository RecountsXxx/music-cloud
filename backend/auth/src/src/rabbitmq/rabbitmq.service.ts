import { Inject, Injectable, Logger } from '@nestjs/common';
import { ClientProxy } from '@nestjs/microservices';

@Injectable()
export class RabbitMQService  {
  private readonly logger = new Logger(RabbitMQService.name);

  constructor(
    @Inject('RABBITMQ_SERVICE') private readonly client: ClientProxy
  ) {
    this.client.connect()
      .then(r => console.log("RABBITMQ CONNECTION IS SUCCESSFUL"))
      .catch(e => console.log("RABBITMQ CONNECTION HAVE FAILED", e));
  }

  sendMessage(queue: string, message: any) {
    this.logger.log(`Sending message to queue ${queue}: ${JSON.stringify(message)}`);
    this.client.emit(queue, message);
  }
}