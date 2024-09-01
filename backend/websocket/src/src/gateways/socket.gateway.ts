import {
  WebSocketGateway,
  WebSocketServer,
  OnGatewayConnection,
  OnGatewayDisconnect,
  OnGatewayInit,
} from '@nestjs/websockets';
import { Server, Socket } from 'socket.io';
import {
  AuthGrpcService,
  ValidateTokenRequest,
  ValidateTokenResponse,
} from '../auth/auth.service';
import { firstValueFrom } from 'rxjs';

@WebSocketGateway({
  cors: {
    origin: '*',
  },
})
export class SocketGateway
  implements OnGatewayInit, OnGatewayConnection, OnGatewayDisconnect
{
  @WebSocketServer() server: Server;

  constructor(private authGrpcService: AuthGrpcService) {}

  async handleConnection(socket: Socket) {
    try {
      const validateTokenRequest: ValidateTokenRequest = {
        token: socket.handshake.auth.token,
      };

      const response: ValidateTokenResponse = await firstValueFrom(
        this.authGrpcService.validateToken(validateTokenRequest),
      );

      if (!response.valid) {
        socket.disconnect();
        return;
      }

      const userId = response.userDTO.id;
      socket.join(userId);
    } catch (error) {
      socket.disconnect();
    }
  }

  handleDisconnect(socket: Socket) {
    // Disconnection handling
  }

  // Method for sending a message to a room with an event
  sendMessageToRoom(roomId: string, event: string, message: any) {
    this.server.to(roomId).emit(event, message);
  }

  afterInit(server: any): any {}
}
