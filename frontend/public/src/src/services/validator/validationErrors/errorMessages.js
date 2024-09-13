import { useI18n } from 'vue-i18n'


export function usernameErrors(validation) {
  const errors = []
  const { t } = useI18n()

  if (validation.username.required.$invalid) {
    errors.push(t('RegisterForm.Errors.username.Required'))
  }
  if (validation.username.minLength.$invalid) {
    errors.push(t('RegisterForm.Errors.username.MinLength'))
  }
  if (validation.username.maxLength.$invalid) {
    errors.push(t('RegisterForm.Errors.username.MaxLength'))
  }
  if (validation.username.regex.$invalid) {
    errors.push(t('RegisterForm.Errors.username.Regex'))
  }
  if (validation.username.isUnique.$invalid) {
    errors.push(t('RegisterForm.Errors.username.uniqueUsername'))
  }
  return errors
}

export function emailErrors(validation) {
  const errors = []
  const { t } = useI18n()

  if (validation.email.required.$invalid) {
    errors.push(t('RegisterForm.Errors.email.Required'))
  }
  if (validation.email.minLength.$invalid) {
    errors.push(t('RegisterForm.Errors.email.MinLength'))
  }
  if (validation.email.maxLength.$invalid) {
    errors.push(t('RegisterForm.Errors.email.MaxLength'))
  }
  if (validation.email.regex.$invalid) {
    errors.push(t('RegisterForm.Errors.email.Regex'))
  }
  if (validation.email.isUnique.$invalid) {
    errors.push(t('RegisterForm.Errors.email.uniqueUsername'))
  }
  return errors
}