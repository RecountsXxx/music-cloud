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

// Функция дла сохранения языка пользователя в локальном хранилище
export function saveLanguageToLocalStorage(language) {
  localStorage.setItem('language', language)
}

// Функция для загрузки языка пользователя из локального хранилища
export function loadLanguageFromLocalStorage(language) {
  const lang = localStorage.getItem('language')
  return lang ? lang : null
}

// Определение хранилища пользователя с помощью Pinia
export const useUserStore = defineStore('useUserStore', {
  state: () => ({
    user: null, // Начальное состояние - пользователь не задан
    language: null
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
      // loading language
      const language = loadLanguageFromLocalStorage()
      if (!language) {
        this.language = getPreferredLanguage()
        saveLanguageToLocalStorage(this.language)
      }
      const user = loadUserFromLocalStore()
      if (user) {
        this.user = user
      }
    }
  }
})
