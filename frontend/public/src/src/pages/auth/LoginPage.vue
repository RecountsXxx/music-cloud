<template>
  <main id="main__container">
    <!-- Заголовок формы входа -->
    <div class="login__title">{{ $t('loginForm.form__title') }}</div>

    <!-- Форма для входа в систему -->
    <form id="login__form" @submit.prevent="loginSubmit">
      <!-- Поле ввода для email -->
      <input
          class="form__input"
          v-model="email"
          id="email"
          type="text"
          :placeholder="$t('loginForm.placeholder.email')"
          @input="clearError"
      />

      <!-- Поле ввода для пароля с возможностью показа/скрытия пароля -->
      <div class="password__field">
        <input
            class="form__input"
            v-model="password"
            id="password"
            type="password"
            ref="passwordInput"
            :placeholder="$t('loginForm.placeholder.password')"
            @input="clearError"
        />

        <!-- Кнопка для показа/скрытия пароля -->
        <div class="image__wrapper">
          <img
              class="showHidePassword"
              src="../../assets/images/showPassword.svg"
              alt=""
              @click="changeVisiblePassword"
          />
        </div>
      </div>

      <!-- Блок для чекбокса "Запомнить меня" и ссылки "Забыли пароль?" -->
      <div class="remember-forgot-container">
        <!-- Чекбокс "Запомнить меня" -->
        <div class="remember-me">
          <input id="remember-me" name="remember-me" type="checkbox" v-model="rememberMe"/>
          <label for="remember-me">{{ $t('loginForm.remember-me') }}</label>
        </div>

        <!-- Ссылка на страницу восстановления пароля -->
        <div class="forgot-password">
          <router-link to="#">{{ $t('loginForm.forgot-password') }}</router-link>
        </div>
      </div>

      <!-- Кнопка отправки формы -->
      <input type="submit" class="submit__button" :value="$t('loginForm.buttonSubmit')"/>
    </form>

    <!-- Сообщение об ошибке -->
    <div id="error__message" ref="errorMessage">{{ $t('loginForm.Errors.IncPassOrEmail') }}</div>

    <!-- Ссылка для создания нового аккаунта -->
    <div class="link__create_account">
      {{ $t('loginForm.register.noRegister') }}
      <router-link :to="{ name: 'Register' }">
        {{ $t('loginForm.register.createAccount') }}
      </router-link>
    </div>
  </main>
</template>

<script>
import {showHidePassword} from '@/utils/showHidePassword.js'
import {saveUserData} from '@/utils/saveUserData.js'
import {login} from '@/utils/query-system/query-actions/authActions.js'
import {useMainStore} from '@/stores/mainStore.js'

const mainStore = useMainStore();

export default {
  data() {
    return {
      isError: false, // состояние ошибки
      email: '', // введенный email
      password: '', // введенный пароль
      rememberMe: false, // запомнить пользователя
    }
  },
  async beforeCreate() {
    mainStore.clearStore();
  },
  methods: {
    // Метод отправки формы для входа в систему
    async loginSubmit() {
      if (this.email.length > 3 && this.password.length > 3) {
        const data = {
          email: this.email,
          password: this.password
        }
        this.clearError() // Скрываем ошибки, если они были
        // Попытка аутентификации пользователя
        try {
          const res = await login(data)
          if (res) {
            saveUserData(res, this.rememberMe)
          } else {
            this.showError() // Показ ошибки при неудачной аутентификации
          }
        } catch (error) {
          this.showError() // Показ ошибки в случае исключения
        }
      } else {
        this.showError() // Показ ошибки при невалидных данных
      }
    },

    // Метод для изменения видимости пароля
    changeVisiblePassword() {
      showHidePassword(this.$refs.passwordInput)
    },

    // Метод для очистки состояния ошибки
    clearError() {
      if (this.isError) {
        this.isError = false
        this.$refs.errorMessage.style.visibility = 'hidden'
      }
    },

    // Метод для показа ошибки
    showError() {
      this.isError = true
      this.$refs.errorMessage.style.visibility = 'visible'
    }
  }
}
</script>

<style scoped lang="scss">
@import '@/assets/styles/auth/Login';
</style>
