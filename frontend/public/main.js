import {createApp} from "vue";
import App from "./App.vue";
import router from "./routes/route";
import i18n, {changeLanguage, getPreferredLanguage} from "./i18n";
import store from "./store/store";


const app = createApp(App);
app.use(i18n);
app.use(router);
app.use(store)

// получаем язык для локализации
const preferredLanguages = getPreferredLanguage();

changeLanguage(preferredLanguages).then(() => {
    app.mount('#app')
});

// app.use(i18n).use(router).mount("#app");