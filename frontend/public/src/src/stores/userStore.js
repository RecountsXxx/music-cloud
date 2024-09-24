import { defineStore } from 'pinia'
import { getPreferredLanguage } from '../../i18n.js'

// Функция для загрузки пользователя из локального хранилища
function loadUserFromLocalStore() {
  const user = localStorage.getItem('userStore')
  return user ? JSON.parse(user) : null
}

// Функция для сохранения пользователя в локальном хранилище
export function saveUserToLocalStore(user) {
  localStorage.setItem('userStore', JSON.stringify(user))
}

// Определение хранилища пользователя с помощью Pinia
export const useUserStore = defineStore('useUserStore', {
  state: () => ({
    user: null, // Начальное состояние - пользователь не задан
  }),
  getters: {
    getUser: (state) => state.user,
    getLanguage: (state) => state.language
  },
  actions: {
    // Метод для установки пользователя
    setUser(user) {
      if (user) {
        this.user = user
        saveUserToLocalStore(user)
      }
    },
    // Метод для очистки пользователя
    clearUser() {
      localStorage.removeItem('userStore')
      this.user = null
    },
    // Метод для инициализации состояния из локального хранилища
    initialize() {
      const user = loadUserFromLocalStore()
      if (user) {
        this.user = user
      }
    }
  }
})
