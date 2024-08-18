<template>
  <main id="main__container">
    <div class="login__title">{{ $t('loginForm.form__title') }}</div>
    <form id="login__form" @submit.prevent="handleSubmit">
      <input class="form__input" v-model="email" id="email" type="text"
             :placeholder="$t('loginForm.placeholder.email')"
             @input="clearError">

      <div class="password__field">
        <input class="form__input" v-model="password" id="password"
               type="password"
               ref="passwordInput"
               :placeholder="$t('loginForm.placeholder.password')"
               @input="clearError">
        <div class="image__wrapper"><img class="showHidePassword" src="../../assets/images/showPassword.svg" alt=""
                                         @click="changeVisiblePassword"></div>
      </div>
      <div class="remember-forgot-container">
        <div class="remember-me">
          <input id="remember-me" name="remember-me" type="checkbox" v-model="rememberMe">
          <label for="remember-me">{{ $t('loginForm.remember-me') }}</label>
        </div>
        <div class="forgot-password">
          <router-link to="#">{{ $t('loginForm.forgot-password') }}</router-link>
        </div>
      </div>
      <input type="submit" class="submit__button" :value="$t('loginForm.buttonSubmit')">
    </form>
    <div id="error__message" ref="errorMessage">{{ $t('Errors.IncPassOrEmail') }}</div>
    <div class="link__create_account">{{ $t('register.noRegister') }}
      <router-link to="">{{ $t('register.createAccount') }}</router-link>
    </div>
  </main>
</template>

<script>
import {showHidePassword} from "../../utils/showHidePassword";
import {validator} from "../../services/validator/validator.js";
import {Authentication} from "../../services/authentication/authentication.js";
import {saveJwtTokenInLocalStorage, useAuthStore} from "../../stores/authStore.js";

export default {
  computed: {},
  data() {
    return {
      isError: false,
      email: '',
      password: '',
      rememberMe: false,
      isPasswordVisible: false
    };
  },
  methods: {
    async handleSubmit() {
      // валидируем данные
      const data = {
        email: this.email,
        password: this.password
      }
      if (validator(data, 1)) {
        if (!this.isError) {
          this.clearError();
          // делаем запрос на аутентификацию
          Authentication(data).then(res => {
            if (res !== false) {
              const remember = document.getElementById('remember-me');
              useAuthStore().setJWT(res.accessToken);
              if (remember.checked) {
                saveJwtTokenInLocalStorage(res.accessToken);
              }
            } else {
              this.isError = true;
              this.$refs.errorMessage.style.visibility = 'visible';
            }
          })
        }
      } else {
        this.isError = true;
        this.$refs.errorMessage.style.visibility = 'visible';
      }
    },
    changeVisiblePassword() {
      this.isPasswordVisible = showHidePassword(this.$refs.passwordInput)
    },
    clearError() {
      if (this.isError) {
        this.isError = false;
        this.$refs.errorMessage.style.visibility = 'hidden';
      }
    }
  }
}
</script>


<style scoped lang="scss">
@import "../../assets/styles/Login.scss";
</style>