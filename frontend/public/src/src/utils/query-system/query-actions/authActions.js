'use strict';
import {PerformQuery} from '@/utils/query-system/performQuery.js';
import {QueryMethods} from '@/utils/query-system/queryMethods.js';
import {QueryPaths} from '@/utils/query-system/queryPaths.js';
import {QueryContentTypes} from '@/utils/query-system/queryContentTypes.js';
import {ApiError} from '@/errors/apiError.js';

/**
 * Аутентифицирует пользователя.
 *
 * @param {Object} data - Данные для аутентификации пользователя (email и пароль).
 * @returns {Object|boolean} - Возвращает ответ сервера с данными или false в случае ошибки.
 */
export async function login(data) {
    try {
        return await PerformQuery(QueryMethods.POST, QueryPaths.login(), data, QueryContentTypes.applicationJson);
    } catch (error) {
        handleError(error);
        return false;
    }
}

export async function register(data) {
    try {
        return await PerformQuery(QueryMethods.POST, QueryPaths.register(), data, QueryContentTypes.applicationJson);
    } catch (error) {
        // handleError(error);
        return handleError(error);
    }
}


/**
 * Обрабатывает ошибку при запросе.
 *
 * @param {Object} error - Ошибка, возникшая при запросе.
 */
function handleError(error) {
    if (error.response) {
        // Сервер вернул ответ с ошибкой
        if (error.response.data.statusCode === 409) {
            if (error.response.data.message === 'Email') {
                return 'Email';
            } else {
                return 'Username';
            }
        } else {
            throw new ApiError(`Error API: ${error.response.data}`, error.response);
        }
    } else if (error.request) {
        // Запрос был отправлен, но ответа не получено
        throw new ApiError(`Request sent, but no response: ${error.response.data}`, error.response);
    } else {
        // Ошибка при настройке запроса
        throw new ApiError(`Query Setup Error: ${error.response.data}`, error.response);
    }
}