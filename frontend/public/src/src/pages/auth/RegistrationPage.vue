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
            @input="()=>{
              clearError('username')
              checkInput('username');
            }"
        />
        <div id="userName" ref="usernameError" class="Errors__Message"></div>
      </div>

      <div class="input__container">
        <!-- Поле ввода для email -->
        <input
            class="form__input"
            v-model="email"
            id="email"
            type="text"
            :placeholder="$t('RegisterForm.placeholder.email')"
            @input="()=>{
              clearError('email');
              checkInput('email');
            }"
        />
        <div id="emailError" ref="emailError" class="Errors__Message"></div>
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
        <input id="acceptCheckBox" name="accept" type="checkbox" v-model="acceptLic"/>
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
import {defineComponent} from 'vue'
import {showHidePassword} from '@/utils/showHidePassword.js'
import {register} from '@/utils/query-system/query-actions/authActions.js'
import {validateEmail} from "@/services/validator/validator.js";


export default defineComponent({
  data() {
    return {
      isError: false, // Флаг наличия ошибки
      email: '', // Данные email пользователя
      username: '', // Имя пользователя
      password: '', // Пароль пользователя
      confirmPassword: '', // Подтверждение пароля
      acceptLic: false, // Принятие условий использования
      isPasswordVisible: false // Флаг видимости пароля
    }
  },
  methods: {
    checkInput(input) {
      if (input === 'email') {
        let co = validateEmail(this.email,true);
        console.log(co)
        if (co === 'format') {
          this.$refs.emailError.textContent = this.$t('RegisterForm.Errors.invalidMailFormat');
          this.$refs.emailError.style.visibility = 'visible';
        }
      } else if (input === 'username') {

      }
    },
    clearError(input) {
      const errorRefs = {
        username: 'usernameError',
        email: 'emailError',
        password: 'passwordError',
        confirmPassword: 'passwordConfirmError',
      };

      let errorElement = this.$refs[errorRefs[input]];
      if (errorElement) {
        let errorElement = this.$refs[errorRefs[input]];
        if (errorElement) {
          if (errorElement.style.visibility === 'visible') {
            errorElement.style.visibility = 'hidden'
            errorElement.textContent = ''
          }
        }
      }
    },
    async registerSubmit() {
      // Проверка принятия условий использования
      if (this.acceptLic) {
        const data = {
          email: this.email,
          password: this.password,
          username: this.username
        }

        try {
          const res = await register(data) // Регистрация пользователя
          if (res) {
            if (res === 'Email') {
              console.log('Email')
              // выводим сообщение о том что почта уже занята
              this.$refs.emailError.textContent = this.$t('RegisterForm.Errors.emailExists');
              this.$refs.emailError.style.visibility = 'visible';
            } else if (res === 'Username') {
              // выводим сообщение о том что имя пользователя уже занято
            }
            // saveUserData(res, true) // Сохранение данных пользователя
          } else {
            // this.showError() // Показ ошибки при неудачной регистрации
          }
        } catch (error) {

        }
      } else {
      }
    },
    changeVisiblePassword(elem) {
      this.isPasswordVisible = showHidePassword(elem) // Переключение видимости пароля
    }
  }
})
</script>

<style scoped lang="scss">
@import '../../assets/styles/auth/Registration';
</style>
