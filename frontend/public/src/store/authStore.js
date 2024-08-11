import {defineStore} from "pinia";

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
                this.isAuthenticated = true;
                this.jwtToken = jwtToken;
            }
            this.isAuthenticated = false;
        }
        , clearJWT() {
            this.isAuthenticated = false;
            this.jwtToken = null;
            localStorage.removeItem("jwtToken");
        }
    }
})