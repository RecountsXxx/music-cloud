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
          />
          <div class="image__wrapper">
            <img
              class="showHidePassword"
              src="../../assets/images/showPassword.svg"
              alt=""
              @click="changeVisiblePassword(this.$refs.passwordInput)"
            />
          </div>
        </div>

        <div class="Errors__Message" ref="passwordError"></div>
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
          />
          <div class="image__wrapper">
            <img
              class="showHidePassword"
              src="../../assets/images/showPassword.svg"
              alt=""
              @click="changeVisiblePassword(this.$refs.confirmPasswordInput)"
            />
          </div>
        </div>
        <div class="Errors__Message align-self-center" ref="passwordConfirmError"></div>
      </div>

      <!-- Чекбокс для принятия условий -->
      <div class="acceptPrivacyPolicy">
        <input id="acceptCheckBox" name="accept" type="checkbox" v-model="acceptLic" />
        <label for="acceptCheckBox">
          {{ $t('RegisterForm.accept.IAccept') }}
          <router-link to="#">{{ $t('RegisterForm.accept.terms') }}</router-link>
          {{ $t('RegisterForm.accept.and') }}
          <router-link to="#">{{ $t('RegisterForm.accept.privacy_policy') }}</router-link>
        </label>
      </div>

      <!-- Кнопка отправки формы -->
      <input type="submit" class="submit__button" :value="$t('RegisterForm.buttonSubmit')" />
    </form>
  </main>
</template>

<script lang="js">
import { ref } from 'vue'
import { helpers, maxLength, minLength, required } from '@vuelidate/validators'
import useVuelidate from '@vuelidate/core'
import { useI18n } from 'vue-i18n'
import { emailErrors, usernameErrors } from '@/services/validator/validationErrors/errorMessages.js'

export default {
  methods: { emailErrors, usernameErrors },
  setup() {

    let uniqueUsername = ref(false)
    let uniqueEmail = ref(false)

    const username = ref('')
    const email = ref('')
    const password = ref('')
    const confirmPassword = ref('')
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
        minLength: minLength(5),
        maxLength: maxLength(36),
        regex: helpers.regex(/(?:[a-zA-Z0-9_]+(?:\.[a-zA-Z0-9_]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?|\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-zA-Z0-9-]*[a-zA-Z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])$/),
        isUnique: value => !uniqueEmail.value
      }
    }

    const validation = useVuelidate(rules, { username, email })

    function registerSubmit() {
      // uniqueUsername.value = true
      // uniqueEmail.value = true
      validation.value.$touch()
      if (!validation.value.$invalid) {
        alert('work')
      } else {
        alert('fail')
      }

      // validation.username.$touch()
      // СДЕЛАТЬ ВЫЗОВ ВАЛАДИАЦИИ ЧТО БЫ ВЫВЕСТИ ОШИБКУ

      // if (!validation.value.$invalid) {
      //   alert('Form submitted!');
      // }
    }

    return {
      username,
      email,
      password,
      confirmPassword,
      acceptLic,
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
