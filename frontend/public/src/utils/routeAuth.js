

// export function checkAuthentication() {
    // const authenticated = isLoggedIn();
    // return authenticated;
    // return false;
// }

import {useAuthStore} from "../store/authStore";

export function handleAuthNavigation(to, from, next) {
    /*
    * если маршрут требующий аутентификацию
    * если токен валиден переходим на главную
    * если токен невалиден переходим на страницу входа
    **/

    const authStore = useAuthStore();

    // console.log(mainStore.test);
    if (to.matched.some(record => record.meta.requiresAuth)) {
        if (!authStore.getIsAuthenticated) {
            next({path: '/login'});
        } else {
            next();
        }
    } else if (to.name === 'Login' || to.name === 'Register') {
        if (authStore.getIsAuthenticated) {
            next({path: '/'});
        } else {
            next();
        }
    } else {
        next();
    }
}