<template>
  <main id="main__container">
    <div class="form__title">{{ $t('RegisterForm.form__title') }}</div>
    <!-- Ссылка на страницу входа -->
    <div class="link__login__account">
      {{ $t('RegisterForm.login.haveAcc') }}
      <router-link :to="{ name: 'Login' }">{{ $t('RegisterForm.login.signIn') }}</router-link>
    </div>

    <form id="register__form" @submit.prevent="registerSubmit">
      <!-- Поле ввода для имени пользователя -->
      <div class="input__container">
        <input
            class="form__input"
            type="text"
            v-model="username"
            :placeholder="$t('RegisterForm.placeholder.username')"
            @input="validation.username.$touch(), uniqueUsername = false"
            @blur="validation.username.$touch()"
        />
        <span v-if="validation.username.$error">
            <div class="Errors__Message"
                 v-for="(error) in usernameErrors(validation)"
            >{{ error }}</div>
          </span>
      </div>

      <div class="input__container">
        <!-- Поле ввода для email -->
        <input
            class="form__input"
            v-model="email"
            id="email"
            type="text"
            :placeholder="$t('RegisterForm.placeholder.email')"
            @input="validation.email.$touch(), uniqueEmail = false"
            @blur="validation.email.$touch()"
        />
        <span v-if="validation.email.$error">
          <div class="Errors__Message"
               v-for="(error) in emailErrors(validation)"
          >{{ error }}</div>
        </span>
      </div>

      <div class="input__container">
        <!-- Поле ввода для пароля -->
        <div class="password__field">
          <input
              class="form__input password"
              v-model="password"
              id="password"
              type="password"
              ref="passwordInput"
              :placeholder="$t('loginForm.placeholder.password')"
              @input="validation.password.$touch()"
              @blur="validation.password.$touch()"
          />
          <div class="image__wrapper">
            <img
                class="showHidePassword"
                src="../../assets/images/showPassword.svg"
                alt=""
                @click="showHidePassword(this.$refs.passwordInput)"
            />
          </div>
        </div>
        <span v-if="validation.password.$error">
            <div class="Errors__Message"
                 v-for="(error) in passwordErrors(validation)"
            >{{ error }}</div>
          </span>
      </div>
      <div class="input__container">
        <!-- Поле ввода для подтверждения пароля -->
        <div class="password__field">
          <input
              class="form__input password"
              v-model="confirmPassword"
              id="confirmPassword"
              type="password"
              ref="confirmPasswordInput"
              :placeholder="$t('RegisterForm.placeholder.confirmPassword')"
              @input="validation.confirmPassword.$touch()"
              @blur="validation.confirmPassword.$touch()"
          />
          <div class="image__wrapper">
            <img
                class="showHidePassword"
                src="../../assets/images/showPassword.svg"
                alt=""
                @click="showHidePassword(this.$refs.confirmPasswordInput)"
            />
          </div>
        </div>
        <span v-if="validation.confirmPassword.$error">
          <div class="Errors__Message"
               v-for="(error) in passwordConfirmErrors(validation)"
          >{{ error }}</div>
        </span>
      </div>

      <!-- Чекбокс для принятия условий -->
      <div class="acceptPrivacyPolicy">
        <input id="acceptCheckBox"
               :class="{ 'highlight': accept }"
               name="accept" type="checkbox"
               v-model="acceptLic"
        />
        <label for="acceptCheckBox">
          {{ $t('RegisterForm.accept.IAccept') }}
          <router-link to="#">{{ $t('RegisterForm.accept.terms') }}</router-link>
          {{ $t('RegisterForm.accept.and') }}
          <router-link to="#">{{ $t('RegisterForm.accept.privacy_policy') }}</router-link>
        </label>
      </div>

      <!-- Кнопка отправки формы -->
      <input type="submit" class="submit__button" :value="$t('RegisterForm.buttonSubmit')"/>
    </form>
  </main>
</template>

<script lang="js">
import {ref} from 'vue'
import {helpers, maxLength, minLength, required} from '@vuelidate/validators'
import useVuelidate from '@vuelidate/core'
import {
  emailErrors,
  passwordConfirmErrors,
  passwordErrors,
  usernameErrors
} from '@/services/validator/validationErrors/errorMessages.js'
import {showHidePassword} from '@/utils/showHidePassword.js'
import {register} from "@/utils/query-system/query-actions/authActions.js";
import {saveUserData} from "@/utils/saveUserData.js";

export default {
  methods: {passwordConfirmErrors, passwordErrors, showHidePassword, emailErrors, usernameErrors},
  setup() {
    let uniqueUsername = ref(false)
    let uniqueEmail = ref(false)
    let accept = ref(false)

    const username = ref('')
    const email = ref('')
    const password = ref('')
    const confirmPassword = ref('')
    const isValidPassword = ref('')

    const acceptLic = ref(false)
    const rules = {
      username: {
        required,
        minLength: minLength(3),
        maxLength: maxLength(15),
        regex: helpers.regex(/^[a-zA-Z][a-zA-Z0-9_-]*$/),
        isUnique: value => !uniqueUsername.value
      },
      email: {
        required,
        // minLength: minLength(3),
        regex: helpers.regex(/(?:[a-zA-Z0-9_]+(?:\.[a-zA-Z0-9_]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?|\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-zA-Z0-9-]*[a-zA-Z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])$/),
        isUnique: value => !uniqueEmail.value
      },
      password: {
        required,
        minLength: minLength(8),
        hasUpperCase: value => /[A-Z]/.test(value), // Проверка на заглавную букву
        hasLowerCase: value => /[a-z]/.test(value), // Проверка на строчную букву
        hasDigit: value => /\d/.test(value),       // Проверка на наличие цифры
        hasSpecialChar: value => /[@$!%*?&#]/.test(value), // Проверка на наличие специального символа
        isValidPassword: value => /^[A-Za-z0-9@$!%*?&#]+$/.test(value)
      },
      confirmPassword: {
        required,
        sameAsPassword: value => value === password.value
      }
    }

    const validation = useVuelidate(rules, {username, email, password, confirmPassword})

    async function registerSubmit() {
      if (acceptLic.value === true) {
        validation.value.$touch()
        if (!validation.value.$invalid) {
          // если username или email ужа заняты вернет ошибку
          const data = {
            username: username.value,
            email: email.value,
            password: password.value,
            confirmPassword: confirmPassword.value
          }

          try {
            const res = await register(data)
            if (res) {
              if (res === 'Email') {
                uniqueEmail.value = true
              } else if (res === 'Username') {
                uniqueUsername.value = true
              } else {
                saveUserData(res, this.rememberMe)
              }
            }
          } catch (error) {
            console.log(error)
          }
        }
      } else {
        accept.value = true
        setTimeout(() => {
          accept.value = false
        }, 350)
      }
    }

    return {
      confirmPassword,
      isValidPassword,
      username,
      email,
      password,
      acceptLic,
      accept,
      registerSubmit,
      validation,
      uniqueUsername,
      uniqueEmail
    }
  }
}
</script>

<style scoped lang="scss">
@import '../../assets/styles/auth/Registration';
</style>
