import {defineStore} from "pinia";

function loadJwtTokenFromLocalStorage() {
    return localStorage.getItem("jwtToken");
}

function saveJwtTokenInLocalStorage(token) {
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
            // console.log("setJWT", jwt);
            if (jwt) {
                this.jwtToken = jwt;
                saveJwtTokenInLocalStorage(jwt);
            }
        }
        ,
        Initialization() {
            const jwtToken = loadJwtTokenFromLocalStorage();
            if (jwtToken) {
                this.isAuthenticated = true;
                this.jwtToken = jwtToken;
            }
        }
        , clearJWT() {
            this.isAuthenticated = false;
            localStorage.removeItem("jwtToken");
        }
    }
})