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
      const token = socket.handshake.auth.token;

      if (!token) {
        socket.emit('error', 'No token provided');
        socket.disconnect();
        return;
      }

      const validateTokenRequest: ValidateTokenRequest = {
        token: token,
      };

      const response: ValidateTokenResponse = await firstValueFrom(
        this.authGrpcService.validateToken(validateTokenRequest),
      );

      const tokenStatus = response.tokenStatus;

      switch (tokenStatus) {
        case 'VALID_TOKEN':
          {
            const userId: string = response.userDTO.id;
            socket.join(userId);
            console.log(`Socket connected: ${socket.id}, User ID: ${userId}`);
          }
          break;
        case 'EXPIRED_TOKEN':
          {
            socket.emit('token.validation', {
              message: 'Your session has expired. Please refresh your token.',
              status: tokenStatus,
            });
            socket.disconnect();
          }
          break;
        case 'INVALID_TOKEN':
          {
            socket.emit('token.validation', {
              message: 'Invalid token provided.',
              status: tokenStatus,
            });
            socket.disconnect();
          }
          break;
        case 'ERROR_TOKEN':
          {
            socket.emit('token.validation', {
              message: 'An error occurred while validating token.',
              status: tokenStatus,
            });
            socket.disconnect();
          }
          break;
        default: {
          socket.emit('error', { message: 'Unknown token status.' });
          socket.disconnect();
        }
      }
    } catch (error) {
      console.error('Error during token validation:', error);
      socket.emit('error', 'An error occurred during authentication.');
      socket.disconnect();
    }
  }

  handleDisconnect(socket: Socket) {
    console.log('SOCKET DISCONNECTION');
  }

  // Method for sending a message to a room with an event
  sendMessageToRoom(roomId: string, event: string, message: any) {
    this.server.to(roomId).emit(event, message);
  }

  afterInit(server: any): any {}
}
