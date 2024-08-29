export function errorMessages(elem, inputType, vueInstance, errorType) {
    const errorMessages = {
        Email: {
            Exists: vueInstance.$t('RegisterForm.Errors.emailExists'),
            Format: vueInstance.$t('RegisterForm.Errors.invalidMailFormat')
        },
        Username: {
            Exists: vueInstance.$t('RegisterForm.Errors.usernameExists'),
            Format: vueInstance.$t('RegisterForm.Errors.invalidUsernameFormat'),
            Length: vueInstance.$t('RegisterForm.Errors.usernameLength')
        },
        Password: {
            Format: vueInstance.$t('RegisterForm.Errors.InvalidPasswordFormat'),
        }
    };
    if (errorType === 'exist') {
        elem.textContent = errorMessages[inputType].Exists;
        elem.style.visibility = 'visible';
    } else if (errorType === 'length') {
        elem.textContent = errorMessages[inputType].Length;
        elem.style.visibility = 'visible';
    } else if (errorType === 'format') {
        elem.textContent = errorMessages[inputType].Format;
        elem.style.visibility = 'visible';
    }

    // if(inputType)
    //
    // if (inputType && format === false) {
    //     elem.textContent = errorMessages[inputType].noFormat;
    //     elem.style.visibility = 'visible';
    // }
    // if (inputType && format === true) {
    //     elem.textContent = errorMessages[inputType].Format;
    //     elem.style.visibility = 'visible';
    // } else if (inputType && length === false) {
    //     elem.textContent = errorMessages[inputType].Length;
    //     elem.style.visibility = 'visible';
    // }
}
