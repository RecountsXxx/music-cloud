'use strict'
import axios from "axios";
import {checkRememberMe} from "../../utils/checkRememberMe";

// import {authStore} from "../../main";
/**
 * Аутентифицирует пользователя.
 *
 * @param {Object} data - Данные для аутентификации пользователя (email и пароль).
 * @returns {boolean} - Возвращает `true`, если аутентификация успешна, иначе `false`.
 */
export function Authentication(data) {
    // Отправляет данные для аутентификации на сервер и получает token.
    // Предположим, что сервер возвращает успешный ответ с token'ом.
    // В этом примере просто устанавливаем аутентификацию в true для демонстрации.


    const url = "http://localhost:3000/api/auth/login";

    // Отправка POST-запроса
    sendPostRequest(url, data);
        // .then(result => {
        // if (result === false) {
        //     return false;
        // } else {
        //     console.log(result);
        //     сохраняем jwt toke и user в localStorage если стоит remember me
            // console.log(checkRememberMe());
            // if (checkRememberMe()) {
            //
            // }
        // }
    // });
}

async function sendPostRequest(url, respData) {
    try {
        const response = await axios.post(url, respData, {
            headers: {
                'content-type': 'application/json'
            }
        });
        return response.data;

    } catch (error) {
        return false;
    }
}

export function isLoggedIn() {
    // МЕТОД ДЛЯ ПРОВЕРКИ АУТЕНТИФИКАЦИИ И ПРОВЕРКИ ЕСТЬ ЛИ ТОКЕН ДЛЯ РОУТИНГА ПО СТРАНИЦАМ И ОТПРАВКИ ДАННЫХ

    /*
    * проверяем есть ли токен
    * если токен есть, отправляем его на backend для верификации
    * если он валиден, возвращаем true
    * если нет, возвращаем false, что перенаправит нас на страницу входа
    **/
    return false;
}

export function logOut() {
    // удаляет JWT токен
    // authStore.clearToken();
    // удаляем данные пользовтаеля

}