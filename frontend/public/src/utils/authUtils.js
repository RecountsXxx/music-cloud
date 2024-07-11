import {isLoggedIn} from "../services/Authentication/Authentication";
import store from "../store/store";

export function checkAuthentication() {
    const authenticated = isLoggedIn();
    store.dispatch('setAuthentication', authenticated);
    return authenticated;
}

export function handleAuthNavigation(to, from, next) {
    /*
    * если маршрут требующий аутентификацию
    * если токен валиден переходим на главную
    * если токен невалиден переходим на страницу входа
    **/

    if (to.matched.some(record => record.meta.requiresAuth)) {
        if (!store.getters.isAuthenticated) {
            next({path: '/login'});
        } else {
            next();
        }
    } else if (to.name === 'Login' || to.name === 'Register') {
        if (store.getters.isAuthenticated) {
            next({path: '/'});
        } else {
            next();
        }
    } else {
        next();
    }
}