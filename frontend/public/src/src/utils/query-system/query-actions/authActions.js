'use strict';
import {PerformQuery} from "@/utils/query-system/performQuery.js";
import {QueryMethods} from "@/utils/query-system/queryMethods.js";
import {QueryPaths} from "@/utils/query-system/queryPaths.js";
import {QueryContentTypes} from "@/utils/query-system/queryContentTypes.js";

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
    handleError(error);
    return false;
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
    console.error("Ошибка ответа сервера:", error.response.data);
  } else if (error.request) {
    // Запрос был отправлен, но ответа не получено
    console.error("Запрос отправлен, но ответа нет:", error.request);
  } else {
    // Ошибка при настройке запроса
    console.error("Ошибка настройки запроса:", error.message);
  }
}