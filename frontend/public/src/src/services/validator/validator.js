export function validator(data, validatorType) {
    switch (validatorType) {
        case 1:
            return validateEmail(data.email, false) && validatePassword(data.password);
        default:
            return false;
    }
}

export function validUsername(username) {
    const usernameRegPattern = /^[a-zA-Z][a-zA-Z0-9_-]{2,19}$/;

}

export function validateEmail(email, isRegister = true) {
    if (!email) {
        return false;
    }

    // Вынесем регулярное выражение в отдельную переменную
    const emailRegPattern = /(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])/;
    return emailRegPattern.test(email) ? true : (isRegister) ? "format" : false;
}

function validatePassword(password) {
    if (!password) {
        return false;
    }

    const passwordRegex = /^[a-zA-Z0-9!@#$%^&*()_\-+=.]+$/;
    return passwordRegex.test(password);
}