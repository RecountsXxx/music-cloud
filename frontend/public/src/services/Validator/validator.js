export function validator(data, validator) {
    // validator 1 - login , 2 - registration
    if (validator === 1) {
        return validationEmail(data.email) && validationPassword(data.password);
    }
    if (validator === 2) {
        
    }
}


function validationEmail(email) {
    if (!email) {
        return false;
    }

    const emailRegPattern = /(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])/;
    return emailRegPattern.test(email);
}

function validationPassword(password) {
    if (!password) {
        return false;
    }

    const passwordRegex = /^[a-zA-Z0-9!@#$%^&*()_\-+=.]+$/;
    return passwordRegex.test(password);
}