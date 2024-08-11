import {createApp} from "vue";
import App from "./App.vue";
import router from "./routes/route";
import i18n, {changeLanguage, getPreferredLanguage} from "./i18n";
import {createPinia} from "pinia";
import {useMainStore} from "./store/mainStore";
import {useUserStore} from "./store/userStore";
import {User} from "./models/User";
import {useAuthStore} from "./store/authStore";


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

export const mainStore = useMainStore();
mainStore.Initialization();


// app.use(i18n).use(router).mount("#app");