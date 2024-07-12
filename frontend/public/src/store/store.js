// store.js

import Vuex from 'vuex';

export default new Vuex.Store({
    state: {
        isAuthenticated: false, // начальное состояние: пользователь не авторизован
    },
    mutations: {
        // Мутация для изменения состояния авторизации
        setAuthentication(state, status) {
            state.isAuthenticated = status;
        },
    },
    actions: {
        // Действие для установки состояния авторизации
        setAuthentication({ commit }, status) {
            commit('setAuthentication', status);
        },
    },
    getters: {
        // Геттер для получения состояния авторизации
        isAuthenticated: (state) => state.isAuthenticated,
    },
});