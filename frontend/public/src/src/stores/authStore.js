import {defineStore} from "pinia";
import {watch} from "vue";
import router from "../router/router.js";

function loadJwtTokenFromLocalStorage() {
    return localStorage.getItem("jwtToken");
}

export function saveJwtTokenInLocalStorage(token) {
    localStorage.setItem("jwtToken", token);
}

export const useAuthStore = defineStore('useAuthStore', {
    state: () => ({
        isAuthenticated: false,
        jwtToken: null
    }),
    getters: {
        getIsAuthenticated: (state) => state.isAuthenticated,
        getJWT: (state) => state.jwtToken
    },
    actions: {
        setJWT(jwt) {
            if (jwt) {
                this.jwtToken = jwt;
                this.isAuthenticated = true;
            }
        },
        Initialization() {
            const jwtToken = loadJwtTokenFromLocalStorage();
            if (jwtToken) {
                this.setJWT(jwtToken);
            }
        }
        , clearJWT() {
            this.isAuthenticated = false;
            this.jwtToken = null;
            localStorage.removeItem("jwtToken");
        },
        subscribeToAuthChanges() {
            watch(() => this.isAuthenticated, (isAuth) => {
                router.push({path: isAuth ? '/' : '/login'});
            })
        }
    }
})