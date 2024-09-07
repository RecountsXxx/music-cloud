import { defineStore } from "pinia";
import { watch } from "vue";
import router from "@/router/router.js";

// Функция для загрузки JWT токена из Local Storage
function loadJwtTokenFromLocalStorage() {
    return localStorage.getItem("jwtToken");
}

// Функция для сохранения JWT токена в Local Storage
export function saveJwtTokenToLocalStorage(token) {
    localStorage.setItem("jwtToken", token);
}

// Функция для удаления JWT токена из Local Storage
export function removeJwtTokenFromLocalStorage() {
    localStorage.removeItem("jwtToken");
}

// Создаем Pinia store для управления аутентификацией
export const useAuthStore = defineStore('useAuthStore', {
    state: () => ({
        // Первоначальное состояние аутентификации и токена
        isAuthenticated: false,
        jwtToken: null
    }),
    getters: {
        // Геттер для получения состояния аутентификации
        getIsAuthenticated: (state) => state.isAuthenticated,
        // Геттер для получения JWT токена
        getJWT: (state) => state.jwtToken
    },
    actions: {
        // Устанавливает JWT токен и отмечает пользователя как аутентифицированного
        setJWT(jwt) {
            if (jwt) {
                this.jwtToken = jwt;
                this.isAuthenticated = true;
                saveJwtTokenToLocalStorage(jwt); // Сохраняем токен при установке
            }
        },
        // Инициализация состояния на основе токена, сохраненного в Local Storage
        initialize() {
            const jwtToken = loadJwtTokenFromLocalStorage();
            if (jwtToken) {
                this.setJWT(jwtToken);
            }
        },
        // Очистка JWT токена и сброс состояния аутентификации
        clearJWT() {
            this.isAuthenticated = false;
            this.jwtToken = null;
            removeJwtTokenFromLocalStorage();
        },
        // Подписка на изменения состояния аутентификации и перенаправление пользователя
        subscribeToAuthChanges() {
            watch(() => this.isAuthenticated, (isAuth) => {
                router.push({ path: isAuth ? '/' : '/login' });
            });
        }
    }
});
