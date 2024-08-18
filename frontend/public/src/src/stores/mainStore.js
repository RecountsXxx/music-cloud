import {defineStore} from "pinia";
import {useUserStore} from "./userStore";
import {useAuthStore} from "./authStore";
import {useAvatarsStore} from "@/stores/avatarsStore.js";

export const useMainStore = defineStore('useMainStore', {
    state: () => ({}),
    getters: {},
    actions: {
        Initialization() {
            useAuthStore().initialize();
            useUserStore().initialize();
            useAvatarsStore().initialize();
        },
        clearStore() {
            useAvatarsStore().clearAvatars();
            useAuthStore().clearJWT();
            useUserStore().clearUser();
        }
    }
})