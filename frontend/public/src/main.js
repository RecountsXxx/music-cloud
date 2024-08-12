import {createApp} from "vue";
import App from "./App.vue";
import router from "./routes/route";
import i18n, {changeLanguage, getPreferredLanguage} from "./i18n";
import {createPinia} from "pinia";
import {useMainStore} from "./store/mainStore";
import {useAuthStore} from "./store/authStore";

// Создание приложения Vue
const app = createApp(App);
const pinia = createPinia();

// Подключение плагинов и роутера
app.use(i18n);
app.use(router);
app.use(pinia);

// Инициализация основного хранилища
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

    } catch (error) {
        console.error('Ошибка при инициализации приложения:', error);
    }
}

// Инициализация приложения
initializeApp();