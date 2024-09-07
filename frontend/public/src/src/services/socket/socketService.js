import { io } from 'socket.io-client';
import { refreshAuthToken } from '@/utils/query-system/performQuery.js'
import router from '@/router/router.js'


class SocketService {
  socket = null;

  connect(token) {
    if (this.socket) return;

    this.socket = io(import.meta.env.VITE_API_SOCKET_URL, {
      auth: {
        token: token
      },
    });

    this.socket.on('connect', () => {
      console.log('Socket connected:', this.socket.id);
    });

    this.socket.on('token.validation', async (data) => {
      console.log(`Token validation error: ${data.message}`);

      switch (data.status) {
        case 'EXPIRED_TOKEN':
          console.log('Token expired, trying to refresh...');
          refreshAuthToken()
            .then(() => {
              console.log('Token refreshed successfully, reconnecting...');
            }).catch((error) => {
            console.error('Failed to refresh token:', error);
            //authStore.logout();
            router.push('/login');
          });
          break;
        case 'INVALID_TOKEN':
          console.error('Invalid token, redirecting to login...');
          this.disconnect();
          //authStore.logout();
          await router.push('/login');
          break;
        case 'ERROR_TOKEN':
          console.error('Token validation error, redirecting to login...');
          this.disconnect();
          //authStore.logout();
          await router.push('/login');
          break;

        default:
          console.error('Unknown token status, disconnecting...');
          this.disconnect();
          //authStore.logout();
          await router.push('/login');
      }
    });

    this.socket.on('error', async () => {
      console.error('Unknown error status, disconnecting...');
      this.disconnect();
      //authStore.logout();
      await router.push('/login');
    });

    this.socket.on('disconnect', () => {
      console.log('Socket disconnected');
    });
  }

  disconnect() {
    if (this.socket) {
      this.socket.disconnect();
      this.socket = null;
    }
  }

  on(event, callback) {
    if (this.socket) {
      this.socket.on(event, callback);
    }
  }

  off(event, callback) {
    if (this.socket) {
      this.socket.off(event, callback);
    }
  }

  emit(event, data) {
    if (this.socket) {
      this.socket.emit(event, data);
    }
  }
}

const socketService = new SocketService();
export default socketService;