import axios from 'axios'
import { ApiError } from '@/errors/apiError.js'
import { QueryPaths } from '@/utils/query-system/queryPaths.js'
import { saveUserData } from '@/utils/saveUserData.js'
import { useAuthStore } from '@/stores/authStore.js'
import { toastInfo } from '@/utils/toast/toastNotification.js'
import router from "@/router/router.js";

/**
 * A function to make a request to the API.
 * @returns {Promise<AxiosResponse<any,any>>}
 */
async function makeRequest(config) {
    return await axios.request(config);
}

/**
 * Performs a token refresh request using makeRequest.
 * @returns {Promise<void>}
 */
export async function refreshAuthToken() {
    const config = {
        method: 'POST',
        url: `${QueryPaths.baseApi()}${QueryPaths.refreshToken()}`,
        withCredentials: true
    };

    try {
        const response = await makeRequest(config);
        saveUserData(response.data, true);
    } catch (error) {
        console.error('Error refreshing token:', error);
        throw new Error('Unable to refresh token');
    }
}


/**
 * Выполняет запрос к API с указанным методом, путем и данными.
 *
 * @param {string} method - HTTP-метод для использования (например, 'GET', 'POST', 'PUT', 'DELETE').
 * @param {string} path - Путь к API-эндпоинту (добавляется к базовому URL).
 * @param {object|null} data - Данные для отправки в теле запроса (необязательно).
 * @param {null} contentType - Тип содержимого запроса (по умолчанию 'application/json').
 * @param {string|null} token - Токен авторизации (если требуется).
 * @param isRetry
 * @returns {Promise<any>} - Ответ от API.
 * @throws {ApiError} - В случае ошибки на уровне API.
 * @throws {Error} - В случае сетевой ошибки.
 */
export async function PerformQuery(method, path, data = null, contentType = null, token = null, isRetry = false) {
    const authStore = useAuthStore();

    // Формируем полный URL для запроса
    const url = `${QueryPaths.baseApi()}${path}`;

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

    try {
        const response = await makeRequest(config);
        return response.data;
    } catch (error) {
        if (error.response) {
            if(error.response.status && error.response.status === 401) {
                if(error.response.data && error.response.data.message === 'EXPIRED_TOKEN' && !isRetry) {
                    try {
                        await refreshAuthToken();
                        const newToken = authStore.getJWT;

                        return await PerformQuery(method, path, data, contentType, newToken, true);
                    } catch (refreshError) {
                        console.log('Your session has expired, please login again');
                        toastInfo('Your session has expired, please login again');
                        await router.push('/login');
                    }
                }
                else {
                    console.log('We could not authorize your request, please login again');
                    toastInfo('We could not authorize your request, please login again');
                    await router.push('/login');
                }
            }
            else {
                throw new ApiError(`Error API: ${error.response.data}`, error.response);
            }
        } else {
            throw new Error(error);
        }
    }
}