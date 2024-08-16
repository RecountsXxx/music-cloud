import {defineStore} from "pinia";

function loadUserFromLocalStore() {
    return localStorage.getItem("userStore");
}

export function saveUserFromLocalStore(user) {
    localStorage.setItem("userStore", JSON.stringify(user));
}

export const useUserStore = defineStore('useUserStore', {
    state: () => ({
        user: null
    }),
    getters: {
        getUser: (state) => state.user
    },
    actions: {
        setUser(user) {
            if (user) {
                this.user = user;
            }
        },
        clearUser() {
            localStorage.removeItem("userStore");
            this.user = null;
        }
        ,
        Initialization() {
            const user = loadUserFromLocalStore();
            if (user) {
                this.user = user;
            }
        }
    }
})