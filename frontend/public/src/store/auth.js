import {defineStore} from "pinia";

export function saveTokenToLocalStorage(token) {
    localStorage.setItem('jwtToken', token);
}

export function getTokenFromLocalStorage() {
    return localStorage.getItem('jwtToken');
}

export const useAuthStore = defineStore('auth', {
    state: () => ({
        jwtToken: null,
        isAuth: false,
    }),
    getters: {
        // Геттер для получения состояния аутентификации
        getIsAuth: (state) => state.isAuth,
        // Геттер для получения JWT токена
        getToken: (state) => state.isAuth,
    },
    actions: {
        setToken(token) {
            this.jwtToken = token;
            this.isAuth = !!token;  // Если токен есть, пользователь авторизован
            if (token) {
                saveTokenToLocalStorage(token);  // Сохраняем токен в localStorage
            } else {
                localStorage.removeItem('jwtToken');  // Удаляем токен из localStorage
            }
        },
        clearToken() {
            this.jwtToken = null;
            this.isAuth = false;
            localStorage.removeItem('jwtToken');  // Удаляем токен из localStorage
        },
        logout(user) {
            // очищаем user store
            // вызываем функцию очисти user store
            this.clearToken();
        },
        initializeAuth() {
            const token = getTokenFromLocalStorage(); // получаем JWT из localStorage
            if (token) {
                this.setToken(token);
            }
        }
    }
});