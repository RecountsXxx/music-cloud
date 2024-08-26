import axios from 'axios';
import {ApiError} from '@/errors/apiError.js';
import { QueryPaths } from '@/utils/query-system/queryPaths.js'

/**
 * Выполняет запрос к API с указанным методом, путем и данными.
 *
 * @param {string} method - HTTP-метод для использования (например, 'GET', 'POST', 'PUT', 'DELETE').
 * @param {string} path - Путь к API-эндпоинту (добавляется к базовому URL).
 * @param {object|null} data - Данные для отправки в теле запроса (необязательно).
 * @param {null} contentType - Тип содержимого запроса (по умолчанию 'application/json').
 * @param {string|null} token - Токен авторизации (если требуется).
 * @returns {Promise<any>} - Ответ от API.
 * @throws {ApiError} - В случае ошибки на уровне API.
 * @throws {Error} - В случае сетевой ошибки.
 */
export async function PerformQuery(method, path, data = null, contentType = null, token = null) {
    // Формируем полный URL для запроса
    const url = `${QueryPaths.baseApi()}${path}`;

    try {
        // Устанавливаем заголовки запроса
        const headers = {
            'Content-Type': contentType ? `${contentType}` : '',
            Authorization: token ? `Bearer ${token}` : ''
        };

        // Конфигурация запроса
        const config = {
            method: method, // HTTP-метод (GET, POST и т.д.)
            url: url, // Полный URL для запроса
            headers: headers, // Заголовки, включая тип содержимого и авторизацию
            data: data, // Данные для отправки в теле запроса (если есть)
        };

        // Выполняем запрос и возвращаем данные ответа
        const response = await axios.request(config);
        return response.data;
    } catch (error) {
        // Обрабатываем ошибки, связанные с ответом API
        if (error.response) {
            throw new ApiError(`Error API: ${error.response.data}`, error.response);
        } else {
            // Обрабатываем сетевые ошибки (например, если API недоступен)
            throw new Error(`Network Error: ${error.message}`);
        }
    }
}