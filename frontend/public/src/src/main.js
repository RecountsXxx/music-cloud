import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ToastPlugin from 'vue-toast-notification';
import i18n, {changeLanguage, getPreferredLanguage} from "../i18n.js";
import {useMainStore} from "@/stores/mainStore";
import {useAuthStore} from "@/stores/authStore";

import App from './App.vue'
import router from '@/router/router.js'

import "bootstrap/dist/css/bootstrap.min.css"
import "@/assets/scss/styles.scss"
import "bootstrap"
import { useSocketStore } from '@/stores/socketStore.js'

const app = createApp(App)

app.use(i18n)
app.use(createPinia())
app.use(router)
app.use(ToastPlugin);

export const mainStore = useMainStore();

async function initializeApp() {
  try {
    // Получаем язык для локализации
    const preferredLanguage = getPreferredLanguage();

    // Меняем язык и сразу монтируем приложение
    changeLanguage(preferredLanguage).finally(() => {
      app.mount('#app');
    });

    // Инициализация основного хранилища
    mainStore.Initialization();

    // Подписываемся на изменения авторизации
    useAuthStore().subscribeToAuthChanges();

    useSocketStore().watchToken()
  } catch (error) {
    console.error('Error when initializing the application:', error);
  }
}

// Инициализация приложения
await initializeApp();