<template>
  <main id="main__container">
    <!-- Заголовок формы -->
    <div class="login__title">
      {{ $t('loginForm.form__title') }}
    </div>

    <!-- Форма для входа -->
    <form id="login__form" @submit.prevent="loginSubmit">

      <!-- Поле для ввода email -->
      <input class="form__input"
             v-model="email"
             id="email"
             type="text"
             :placeholder="$t('loginForm.placeholder.email')"
             @input="clearError">

      <!-- Поле для ввода пароля с возможностью показать/скрыть пароль -->
      <div class="password__field">
        <input class="form__input"
               v-model="password"
               id="password"
               type="password"
               ref="passwordInput"
               :placeholder="$t('loginForm.placeholder.password')"
               @input="clearError">

        <!-- Иконка для показа/скрытия пароля -->
        <div class="image__wrapper">
          <img class="showHidePassword"
               src="../../assets/images/showPassword.svg"
               alt="Показать/скрыть пароль"
               @click="changeVisiblePassword">
        </div>
      </div>

      <!-- Блок для запоминания пользователя и ссылки на восстановление пароля -->
      <div class="remember-forgot-container">
        <!-- Чекбокс "Запомнить меня" -->
        <div class="remember-me">
          <input id="remember-me"
                 name="remember-me"
                 type="checkbox"
                 v-model="rememberMe">
          <label for="remember-me">{{ $t('loginForm.remember-me') }}</label>
        </div>

        <!-- Ссылка на восстановление пароля -->
        <div class="forgot-password">
          <router-link to="#">
            {{ $t('loginForm.forgot-password') }}
          </router-link>
        </div>
      </div>

      <!-- Кнопка отправки формы -->
      <input type="submit"
             class="submit__button"
             :value="$t('loginForm.buttonSubmit')">
    </form>

    <!-- Сообщение об ошибке -->
    <div id="error__message" ref="errorMessage">
      {{ $t('Errors.IncPassOrEmail') }}
    </div>

    <!-- Ссылка на создание новой учетной записи -->
    <div class="link__create_account">
      {{ $t('loginForm.register.noRegister') }}
      <router-link :to="{ name: 'Register' }">
        {{ $t('loginForm.register.createAccount') }}
      </router-link>
    </div>
  </main>

</template>

<script lang="js">


import {authenticateUser} from "@/services/authentication/Authentication";
import {saveJwtTokenInLocalStorage, useAuthStore} from "@/store/authStore";
import {showHidePassword} from "@/utils/showHidePassword";
import {validator} from "@/services/validator/validator";

export default {
  computed: {},

  // Локальные данные компонента
  data() {
    return {
      isError: false,       // Индикатор ошибки
      email: '',            // Email пользователя
      password: '',         // Пароль пользователя
      rememberMe: false,    // Флаг "Запомнить меня"
      isPasswordVisible: false // Флаг видимости пароля
    };
  },

  methods: {
    // Метод для обработки отправки формы входа
    async loginSubmit() {
      // Подготавливаем данные для валидации и отправки
      const data = {
        email: this.email,
        password: this.password
      };

      // Валидируем данные
      if (validator(data, 1)) {
        if (!this.isError) {
          // Очищаем предыдущие ошибки, если они были
          this.clearError();

          // Запрос на аутентификацию пользователя
          authenticateUser(data).then(res => {
            if (res !== false) {
              // Успешная аутентификация, сохраняем токен
              useAuthStore().setJWT(res.accessToken);

              // Если пользователь выбрал "Запомнить меня", сохраняем токен в LocalStorage
              if (this.rememberMe) {
                saveJwtTokenInLocalStorage(res.accessToken);
              }
            } else {
              // Если аутентификация не удалась, показываем ошибку
              this.showError();
            }
          });
        }
      } else {
        // Если валидация не прошла, показываем ошибку
        this.isError = true;
        this.$refs.errorMessage.style.visibility = 'visible';
      }
    },

    // Метод для смены видимости пароля
    changeVisiblePassword() {
      this.isPasswordVisible = showHidePassword(this.$refs.passwordInput);
    },

    // Метод для очистки ошибок
    clearError() {
      if (this.isError) {
        this.isError = false;
        this.$refs.errorMessage.style.visibility = 'hidden';
      }
    },

    // Метод для показа ошибок
    showError() {
      this.isError = true;
      this.$refs.errorMessage.style.visibility = 'visible';
    }
  }
}

</script>

<style scoped lang="scss">
@import "/src/assets/styles/Login.scss";
</style>