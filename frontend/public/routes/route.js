import {createRouter, createWebHistory} from "vue-router";
import routes from "./routes";
import {isLoggedIn} from "../services/Authentication/Authentication";
import i18n, {changeLanguage} from "../i18n";

const router = new createRouter(
    {
        history: createWebHistory(),
        routes: routes
    }
);

router.beforeEach((to, from, next) => {
    document.title = to.meta.title;
    // делаем проверку токена есть ли он у нас и валиден ли он
    const isAuthenticated = isLoggedIn();

    /*
    * если маршрут требующий аутентификацию
    * если токен валиден переходим на главную
    * если токен невалиден переходим на страницу входа
    * */
    if (to.matched.some(record => record.meta.requiresAuth)) {
        if (!isAuthenticated) {
            next({path: '/login'});
        } else {
            next();
        }
        /*
        * Если переходим на страницу /login | /register
        * если у нас есть валидный токен - переходим на главную страницу /
        * если нет валидного токена - переходим на страницу входа или регистрации
        * */
    } else if (to.name === 'Login' || to.name === 'Register') {
        if (isAuthenticated) {
            next({path: '/'});
        } else {
            next();
        }
    } else {
        next();
    }
});

export default router;