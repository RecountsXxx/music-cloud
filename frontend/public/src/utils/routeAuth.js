import {useAuthStore} from "../store/auth";
import {authStore} from "../main";

// export function checkAuthentication() {
    // const authenticated = isLoggedIn();
    // return authenticated;
    // return false;
// }

export function handleAuthNavigation(to, from, next) {
    /*
    * если маршрут требующий аутентификацию
    * если токен валиден переходим на главную
    * если токен невалиден переходим на страницу входа
    **/

    console.log(authStore.getIsAuth);
    if (to.matched.some(record => record.meta.requiresAuth)) {
        if (!authStore.getIsAuth) {
            next({path: '/login'});
        } else {
            next();
        }
    } else if (to.name === 'Login' || to.name === 'Register') {
        if (authStore.getIsAuth) {
            next({path: '/'});
        } else {
            next();
        }
    } else {
        next();
    }
}