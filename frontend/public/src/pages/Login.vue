<template>
  <main class="main-content">
    <section class="login-container">
      <h1 class="login__title">{{ $t('loginForm.form__title') }}</h1>
      <form class="login__form" @submit.prevent="handleSubmit">
        <div class="login__field">
          <label class="login-form__label" for="email"></label>
          <input class="login-form__input" v-model="email" id="email" type="text" required
                 :placeholder="$t('loginForm.placeholder.email')">
        </div>
        <div class="login_form__field">
          <label class="login-form__label" for="password"></label>
          <div class="password__wrapper">
            <input class="login-form__input" v-model="password" id="password"
                   type="password" required
                   ref="passwordInput"
                   :placeholder="$t('loginForm.placeholder.password')">
            <input id="showPassword" type="button" :value="passShowLabel" @click="changeVisiblePassword">
          </div>
        </div>
        <div class="remember-me">
          <input id="remember-me" name="remember-me" type="checkbox" v-model="rememberMe">
          <label for="remember-me">{{ $t('loginForm.remember-me') }}</label>
        </div>
        <div class="forgot-password">
          <router-link to="#">{{ $t('loginForm.forgot-password') }}</router-link>
        </div>
        <input type="submit" class="login-form_button" :value="$t('loginForm.buttonSubmit')">
      </form>
    </section>
    <div class="register">
      <p>{{ $t('register.noRegister') }}
        <router-link to="#">{{ $t('register.createAccount') }}</router-link>
      </p>
    </div>
  </main>
</template>

<script>
import {showHidePassword} from "../composables/showHidePassword";
import {Authentication} from "../services/Authentication/Authentication";

export default {
  data() {
    return {
      email: '',
      password: '',
      rememberMe: false,
      isPasswordVisible: false
    };
  },
  computed: {
    passShowLabel() {
      return this.isPasswordVisible ? this.$t('loginForm.bVisionPassword.hidden') : this.$t('loginForm.bVisionPassword.visible');
    }
  },
  methods: {
    async handleSubmit() {
      const data = {
        email: this.email,
        password: this.password
      }
      Authentication(data);
    },
    changeVisiblePassword() {
      this.isPasswordVisible = showHidePassword(this.$refs.passwordInput)
    }
  }
}
</script>


<style scoped lang="scss">
@import "../styles/Login.scss";
</style>