'use strict'

/**
 * Класс Validator содержит сообщения об ошибках валидации email и пароля.
 * и саму валидацию
 */
export class Validator {
    /// ПОМЕНЯТЬ ОШИБКИ НА переменные из locales

    // Переменные для сообщений об ошибках
    emailError = "Некорректный формат почты";
    passwordError = {
        incorrectCharacter: "Некорректные символы", // Текст ошибки для некорректных символов в пароле
        smallLength: "Минимальное количество символов (4)", // Текст ошибки для пароля с недостаточной длиной
        bigLength: "Максимальное количество символов (64)" // Текст ошибки для пароля с избыточной длиной
    };

    /**
     * Функция для валидации email.
     * @param {string} email - Email для валидации.
     * @param {object} validator - Объект с сообщениями об ошибках валидации email.
     * @returns {object} - Объект с результатом валидации и текстом ошибки, если таковая есть.
     */
    validationEmail(email, validator) {
        // Проверка наличия значения email
        if (!email) {
            return {
                isValid: true // Email допустим, если пустой
            };
        }

        // Паттерн для проверки регулярного выражения для email
        const emailRegPattern = /(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])/;

        // Проверка email на соответствие регулярному выражению
        const isValid = emailRegPattern.test(email);

        // Возвращаем результат валидации и текст ошибки, если таковая есть
        return {
            isValid: isValid,
            text: isValid ? '' : validator.emailError // Если email не прошел проверку, возвращаем текст ошибки
        };
    }

    /**
     * Функция для валидации пароля.
     * @param {string} password - Пароль для валидации.
     * @param {object} validator - Объект с сообщениями об ошибках валидации пароля.
     * @returns {object} - Объект с результатом валидации и текстом ошибки, если таковая есть.
     */
    validationPassword(password, validator) {
        // Проверка наличия значения пароля
        if (!password) {
            return {
                isValid: true // Пароль допустим, если пустой
            };
        }

        // Установка минимальной и максимальной длины пароля
        const minLength = 4;
        const maxLength = 64;

        // Проверка длины пароля
        if (password.length < minLength) {
            return {
                isValid: false,
                text: validator.passwordError.smallLength // Возвращаем текст ошибки, если длина пароля слишком короткая
            };
        }

        if (password.length > maxLength) {
            return {
                isValid: false,
                text: validator.passwordError.bigLength // Возвращаем текст ошибки, если длина пароля слишком длинная
            };
        }

        // Проверка регулярного выражения для пароля
        const passwordRegex = /^[a-zA-Z0-9!@#$%^&*()_\-+=.]+$/;
        const isValid = passwordRegex.test(password);

        // Возвращаем результат валидации и текст ошибки, если таковая есть
        return {
            isValid: isValid,
            text: isValid ? '' : validator.passwordError.incorrectCharacter // Возвращаем текст ошибки, если пароль не соответствует регулярному выражению
        };
    }


    /**
     * Функция для проверки сложности пароля.
     * @param {string} password - Пароль для анализа сложности.
     * @returns {number} - Числовое значение, обозначающее сложность пароля.
     */
    checkPasswordStrength = (password) => {
        // Инициализируем переменную для хранения сложности пароля
        let strength = 0;

        // Проверяем наличие строчных букв в пароле
        if (password.match(/[a-z]+/)) {
            strength += 1;
        }

        // Проверяем наличие прописных букв в пароле
        if (password.match(/[A-Z]+/)) {
            strength += 1;
        }

        // Проверяем наличие цифр в пароле
        if (password.match(/[0-9]+/)) {
            strength += 1;
        }

        // Проверяем наличие специальных символов в пароле
        if (password.match(/[$@#&!]+/)) {
            strength += 1;
        }

        // Возвращаем числовое значение сложности пароля
        return strength;
    }

}