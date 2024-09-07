import { defineStore } from 'pinia';
import { watch } from 'vue';
import socketService from '@/services/socket/socketService';
import { useAuthStore } from '@/stores/authStore';

export const useSocketStore = defineStore('socketStore', {
  state: () => ({
    isConnected: false,
  }),
  actions: {
    async connect(token) {
      if (token && !this.isConnected) {
        await socketService.connect(token);
        this.isConnected = true;
      }
    },

    disconnect() {
      socketService.disconnect();
      this.isConnected = false;
    },

    subscribe(event, callback) {
      socketService.on(event, callback);
    },

    unsubscribe(event, callback) {
      socketService.off(event, callback);
    },

    send(event, data) {
      socketService.emit(event, data);
    },

    watchToken() {
      const authStore = useAuthStore();

      // Watch for changes in the JWT token
      watch(
        () => authStore.getJWT,
        (newToken, oldToken) => {
          if (newToken !== oldToken) {
            this.disconnect();
            this.connect(newToken);
          }
        },
        { immediate: true }  // Ensure immediate connection on initialization
      );
    }
  },
});