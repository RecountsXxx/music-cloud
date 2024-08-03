import {createApp} from "vue";
import App from "./App.vue";
import router from "./routes/route";
import i18n, {changeLanguage, getPreferredLanguage} from "./i18n";
import {createPinia} from "pinia";
import {useAuthStore} from "./store/auth";


const app = createApp(App);
const pinia = createPinia();

app.use(i18n);
app.use(router);
app.use(pinia);

// получаем язык для локализации
const preferredLanguages = getPreferredLanguage();

changeLanguage(preferredLanguages).then(() => {
    app.mount('#app')
});

export const authStore = useAuthStore();
authStore.initializeAuth(); // инициализируем authStore

// app.use(i18n).use(router).mount("#app");