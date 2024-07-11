'use strict'
import store from "../../store/store";
import axios from "axios";

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

    const url = '';
    const email = data.email;
    const password = data.password;

    // проводим валидацию данных
    const valid = true; // временно

    if (valid) {
        const response = (async () => {
            try {
                const result = await axios.post(url, JSON.stringify(data));
                console.log(result.data); // или любая другая обработка ответа
            } catch (error) {
                console.error('Ошибка:', error);
            }
        })();
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