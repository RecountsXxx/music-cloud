import { defineStore } from 'pinia'
import { useUserStore } from '@/stores/userStore'
import { useAuthStore } from '@/stores/authStore'
import { useAvatarsStore } from '@/stores/avatarsStore.js'

export const useMainStore = defineStore('useMainStore', {
  state: () => ({}),
  getters: {
    getLanguage: (state) => state.language
  },
  actions: {
    Initialization() {
      useAuthStore().initialize()
      useUserStore().initialize()
      useAvatarsStore().initialize()
    },
    clearStore() {
      useAvatarsStore().clearAvatars()
      useAuthStore().clearJWT()
      useUserStore().clearUser()
    }
  }
})
