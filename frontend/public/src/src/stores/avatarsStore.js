import {defineStore} from "pinia";

// Функция для загрузки аватаров из LocalStorage
function loadAvatarsFromLocalStorage() {
    return JSON.parse(localStorage.getItem("avatarsStore"));
}

// Функция для сохранения аватаров в LocalStorage
export function saveAvatarsInLocalStorage(avatars) {
    localStorage.setItem("avatarsStore", JSON.stringify(avatars));
}

// Определение store для работы с аватарами
export const useAvatarsStore = defineStore("useAvatarsStore", {
    state: () => ({
        avatars: null,
    }),

    getters: {
        // Геттер для получения аватаров
        getAvatars: state => state.avatars,
    },

    actions: {
        setAvatars(avatars) {
            if (avatars) {
                this.avatars = JSON.stringify(avatars);
                saveAvatarsInLocalStorage(avatars); // Сохранение в LocalStorage при установке
            }
        },

        // Метод для очистки аватаров из store и LocalStorage
        clearAvatars() {
            this.avatars = null;
            localStorage.removeItem("avatarsStore");
        },

        // Метод для инициализации store при загрузке
        initialize() {
            const avatars = loadAvatarsFromLocalStorage();
            if (avatars) {
                this.avatars = avatars;
            }
        }
    }
});
