import {useI18n} from 'vue-i18n'

function generateErrors(validation, fieldName, rules) {
    const errors = []
    const {t} = useI18n()

    // Проверка на required
    const requiredRule = rules.find(rule => rule.validator === 'required')
    if (requiredRule && validation[fieldName]?.required?.$invalid) {
        errors.push(t(requiredRule.message))
        return errors // Возвращаем только ошибку required и ничего больше
    }

    // Проверка остальных правил, если поле заполнено
    rules.forEach(rule => {
        const {validator, message, condition} = rule
        if (validator !== 'required' && validation[fieldName]?.[validator]?.$invalid) {
            if (!condition || condition(validation[fieldName])) {
                errors.push(t(message))
            }
        }
    })
    return errors
}

export function usernameErrors(validation) {
    return generateErrors(validation, 'username', [
        {validator: 'required', message: 'RegisterForm.Errors.username.Required'},
        {validator: 'minLength', message: 'RegisterForm.Errors.username.MinLength'},
        {validator: 'maxLength', message: 'RegisterForm.Errors.username.MaxLength'},
        {validator: 'regex', message: 'RegisterForm.Errors.username.Regex'},
        {validator: 'isUnique', message: 'RegisterForm.Errors.username.uniqueUsername'}
    ])
}

export function emailErrors(validation) {
    return generateErrors(validation, 'email', [
        {validator: 'required', message: 'RegisterForm.Errors.email.Required'},
        {validator: 'minLength', message: 'RegisterForm.Errors.email.MinLength'},
        {validator: 'maxLength', message: 'RegisterForm.Errors.email.MaxLength'},
        {validator: 'regex', message: 'RegisterForm.Errors.email.Regex'},
        {validator: 'isUnique', message: 'RegisterForm.Errors.email.uniqueUsername'}
    ])
}

export function passwordErrors(validation) {
    return generateErrors(validation, 'password', [
        {validator: 'required', message: 'RegisterForm.Errors.password.Required'},
        {
            validator: 'minLength',
            message: 'RegisterForm.Errors.password.MinLength',
            condition: (field) => field.$model.length > 0
        },
        {
            validator: 'hasUpperCase',
            message: 'RegisterForm.Errors.password.Uppercase',
            condition: (field) => field.$model.length > 0
        },
        {
            validator: 'hasLowerCase',
            message: 'RegisterForm.Errors.password.Lowercase',
            condition: (field) => field.$model.length > 0
        },
        {
            validator: 'hasDigit',
            message: 'RegisterForm.Errors.password.Digit',
            condition: (field) => field.$model.length > 0
        },
        {
            validator: 'hasSpecialChar',
            message: 'RegisterForm.Errors.password.SpecialChar',
            condition: (field) => field.$model.length > 0
        },
        {
            validator: 'isValidPassword',
            message: 'RegisterForm.Errors.password.IncorrectSymbol',
            condition: (field) => field.$model.length > 0
        }
    ])
}

export function passwordConfirmErrors(validation) {
    return generateErrors(validation, 'confirmPassword', [
        {validator: 'required', message: 'RegisterForm.Errors.password.Required'},
        {validator: 'sameAsPassword', message: 'RegisterForm.Errors.password.Mismatch'}
    ])
}
