import {defineStore} from "pinia";
import {useUserStore} from "./userStore";
import {useAuthStore} from "./authStore";

export const useMainStore = defineStore('useMainStore', {
    state: () => ({}),
    getters: {},
    actions: {
        Initialization() {
            useAuthStore().Initialization();
            useUserStore().Initialization();
        },
        clearStore() {
            useAuthStore().clearJWT();
            useUserStore().clearUser();
        }
    }
})