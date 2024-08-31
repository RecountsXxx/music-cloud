import { Injectable, Logger } from '@nestjs/common';
import { EventPattern, Payload } from '@nestjs/microservices';
import { SocketGateway } from '../gateways/socket.gateway';

@Injectable()
export class RabbitMQListener {
  private readonly logger = new Logger(RabbitMQListener.name);

  constructor(private readonly socketGateway: SocketGateway) {}

  @EventPattern('socket-message')
  handleSocketMessage(@Payload() data: { channel: string; payload: any }) {
    this.logger.log(`Received message for channel: ${data.channel}`);

    const { channel, payload } = data;
    const roomId = payload.roomId;

    // Send a message to a specific room with an event (channel)
    this.socketGateway.sendMessageToRoom(roomId, channel, payload);
  }
}
