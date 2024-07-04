/**
 * Класс для хранения опций валидатора.
 */
export class ValidatorOptions {
    /**
     * Создает новый экземпляр объекта ValidatorOptions.
     * @param {string} validationStr - Строка для валидации.
     * @param {object} validator - Объект валидатора.
     * @param {object} error - Объект для хранения информации об ошибке.
     */
    constructor(validationStr, validator, error) {
        // Строка для валидации
        this.validationStr = validationStr;
        // Объект валидатора
        this.validator = validator;
        // Объект для хранения информации об ошибке
        this.error = error;
    }
}

/**
 * Функция для проверки валидности данных.
 * @param {Object} options - Объект с опциями для проверки.
 * @param {Function} validFunction - Функция для валидации данных.
 */
export function checkValid(options, validFunction) {

    // Вызываем переданную функцию validFunction для валидации данных
    // Передаем в нее строку для валидации и другие необходимые параметры (если есть)
    let isValid = validFunction(options.validationStr, options.validator);

    // Если данные не прошли валидацию
    if (!isValid.isValid) {
        // Устанавливаем флаг ошибки в true
        options.error.isValid = true;
        // Устанавливаем текст ошибки
        options.error.text = isValid.text;
    } else {
        // Если данные прошли валидацию, сбрасываем флаг ошибки
        options.error.isValid = false;
        // Сбрасываем текст ошибки, так как проверка прошла успешно
        options.error.text = null;
    }
}

/**
 * Функция для определения сложности пароля и отображения этой сложности.
 * @param {string} password - Пароль для анализа.
 * @param {object} validator - Объект валидатора, содержащий методы для проверки пароля.
 */
export function passwordStrength(password, validator) {
    // Получаем элемент полосы силы пароля
    let strengthBar = document.getElementById("passwordStrength");

    // Получаем сложность пароля с помощью метода checkPasswordStrength из объекта validator
    let strength = validator.checkPasswordStrength(password);

    // Устанавливаем ширину полосы силы пароля в зависимости от сложности
    strengthBar.style.width = (strength * 25) + "%";

    // Устанавливаем класс для определения цвета полосы силы пароля в зависимости от сложности
    if (strength === 1) {
        strengthBar.className = "weak password-strength";
    } else if (strength === 2 || strength === 3) {
        strengthBar.className = "medium password-strength";
    } else if (strength === 4) {
        strengthBar.className = "strong password-strength";
    }
}