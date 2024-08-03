<template>
  <main id="main__container">
    <div class="login__title">{{ $t('loginForm.form__title') }}</div>
    <form id="login__form" @submit.prevent="handleSubmit">
      <input class="form__input" v-model="email" id="email" type="text" required
             :placeholder="$t('loginForm.placeholder.email')">

      <div class="password__field">
        <input class="form__input" v-model="password" id="password"
               type="password" required
               ref="passwordInput"
               :placeholder="$t('loginForm.placeholder.password')">
        <div class="image__wrapper"><img class="showHidePassword" src="../assets/images/showPassword.svg" alt=""
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
    <div id="error__message">Неверный логин или пароль</div>
    <div class="link__create_account">{{$t('register.noRegister')}}
      <router-link to="">{{$t('register.createAccount')}}</router-link>
    </div>
  </main>
</template>

<script>
import {showHidePassword} from "../utils/showHidePassword";
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
      console.log(data);
      // Authentication(data);
    },
    changeVisiblePassword() {
      this.isPasswordVisible = showHidePassword(this.$refs.passwordInput)
    }
  }
}
</script>


<style scoped lang="scss">
@import "../assets/styles/Login.scss";
</style>