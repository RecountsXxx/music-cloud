import {saveJwtTokenToLocalStorage, useAuthStore} from "@/stores/authStore.js";
import {saveUserToLocalStore, useUserStore} from "@/stores/userStore.js";
import {saveAvatarsInLocalStorage, useAvatarsStore} from "@/stores/avatarsStore.js";

export function saveUserData(res, remember) {
    useUserStore().setUser(res.user);
    useAvatarsStore().setAvatars(res.avatars);
    useAuthStore().setJWT(res.accessToken);

    // Если установлен rememberMe, сохраняем токен и данные в LocalStorage
    if (remember) {
        saveJwtTokenToLocalStorage(res.accessToken);
        saveUserToLocalStore(res.user);
        saveAvatarsInLocalStorage(res.avatars);
    }
}