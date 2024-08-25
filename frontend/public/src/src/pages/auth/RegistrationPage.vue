<template>
  <main id="main__container">
    <div class="form__title">{{ $t('RegisterForm.form__title') }}</div>
    <!-- Ссылка на страницу входа -->
    <div class="link__login__account">
      {{ $t('RegisterForm.login.haveAcc') }}
      <router-link :to="{ name: 'Login' }">{{ $t('RegisterForm.login.signIn') }} </router-link>
    </div>

    <form id="register__form" @submit.prevent="registerSubmit">
      <!-- Поле ввода для имени пользователя -->
      <div class="input__container">
        <input
          class="form__input"
          type="text"
          v-model="username"
          :placeholder="$t('RegisterForm.placeholder.username')"
        />
        <div id="userName" class="Errors__Message">Неверный формат электронной почты</div>
      </div>

      <div class="input__container">
        <!-- Поле ввода для email -->
        <input
          class="form__input"
          v-model="email"
          id="email"
          type="text"
          :placeholder="$t('RegisterForm.placeholder.email')"
        />
        <div id="emailError" class="Errors__Message">Неверный формат электронной почты</div>
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

        <div class="Errors__Message">Минимум от 1 до 8 символов</div>
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
        <div class="Errors__Message align-self-center">Пароли не совпадают</div>
      </div>

      <!-- Чекбокс для принятия условий -->
      <div class="acceptPrivacyPolicy">
        <input id="acceptCheckBox" name="accept" type="checkbox" v-model="acceptLic" />
        <label for="acceptCheckBox">
          {{ $t('RegisterForm.accept.IAccept') }}
          <router-link to="#">{{ $t('RegisterForm.accept.terms') }} </router-link>
          {{ $t('RegisterForm.accept.and') }}
          <router-link to="#">{{ $t('RegisterForm.accept.privacy_policy') }} </router-link>
        </label>
      </div>

      <!-- Кнопка отправки формы -->
      <input type="submit" class="submit__button" :value="$t('RegisterForm.buttonSubmit')" />
    </form>
  </main>
</template>

<script lang="js">
import { defineComponent } from 'vue'
import { saveUserData } from '@/utils/saveUserData.js'
import { showHidePassword } from '@/utils/showHidePassword.js'
import { register } from '@/utils/query-system/query-actions/authActions.js'

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
    async registerSubmit() {
      // Проверка принятия условий использования
      if (this.acceptLic) {
        const data = {
          email: this.email,
          password: this.password,
          username: this.username
        }

        const res = await register(data) // Регистрация пользователя

        // try {
        //   const res = await register(data) // Регистрация пользователя
        //   console.log(res)
        //   if (res) {
        //     saveUserData(res, true) // Сохранение данных пользователя
        //   } else {
        //     // this.showError() // Показ ошибки при неудачной регистрации
        //   }
        // } catch (error) {
        //
        // }
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
