import {defineStore} from "pinia";
import {useUserStore} from "./userStore";
import {useAuthStore} from "./authStore";

export const useMainStore = defineStore('useMainStore', {
    state: () => ({}),
    getters: {},
    actions: {
        Initialization() {
            const userStore = useUserStore();
            const authStore = useAuthStore();
            userStore.Initialization();
            authStore.Initialization();
        },
    }
})