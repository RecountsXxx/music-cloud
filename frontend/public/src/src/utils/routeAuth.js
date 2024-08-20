import {useAuthStore} from "@/stores/authStore";

export function handleAuthNavigation(to, from, next) {
    /*
     * если маршрут требующий аутентификацию
     * если токен валиден переходим на главную
     * если токен невалиден переходим на страницу входа
     **/

    const authStore = useAuthStore();

    // Если маршрут требует аутентификации
    if (to.matched.some(record => record.meta.requiresAuth)) {
        if (!authStore.getIsAuthenticated) {
            next({path: '/login'}); // Перенаправляем на страницу входа
        } else {
            next(); // Разрешаем переход
        }
    } else if (to.name === 'Login' || to.name === 'Register') {
        if (authStore.getIsAuthenticated) {
            next({path: '/'}); // Перенаправляем на главную, если пользователь уже авторизован
        } else {
            next(); // Разрешаем переход на страницу логина/регистрации
        }
    } else {
        next(); // Разрешаем переход на любые другие маршруты
    }
}