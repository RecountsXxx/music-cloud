<template>
  <main id="main__container">
    <div class="form__title">{{ $t("RegisterForm.form__title") }}</div>
    <form id="register__form" @submit.prevent="registerSubmit">
      <input class="form__input" type="text" v-model="username" :placeholder="$t('RegisterForm.placeholder.username')">
      <!-- Поле ввода для email -->
      <input
          class="form__input"
          v-model="email"
          id="email"
          type="text"
          :placeholder="$t('RegisterForm.placeholder.email')"
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
      <input class="form__input" type="text" :placeholder="$t('RegisterForm.placeholder.confirmPassword')">
      <div class="acceptPrivacyPolicy">
        <input id="accept" name="acceptCheckBox" type="checkbox" v-model="acceptLic">
        <label for="accept">{{ $t('RegisterForm.accept.IAccept') }}
          <router-link to="#">{{ $t('RegisterForm.accept.terms') }}</router-link>
          {{ $t('RegisterForm.accept.and') }}
          <router-link to="#">{{ $t('RegisterForm.accept.privacy_policy') }}</router-link>
        </label>
      </div>
      <input type="submit" class="submit__button" :value="$t('RegisterForm.buttonSubmit')">
      <div class="link__login__account">{{ $t("RegisterForm.login.haveAcc") }}
        <router-link :to="{ name: 'Login' }">{{ $t("RegisterForm.login.signIn") }}</router-link>
      </div>
    </form>
  </main>
</template>

<script lang="js">
import {defineComponent} from "vue";
import {showHidePassword} from "@/utils/showHidePassword.js";
import {registerUser} from "@/services/authentication/authentication.js";
import {saveUserData} from "@/utils/saveUserData.js";

export default defineComponent({
  data() {
    return {
      isError: false,
      email: '',
      username: '',
      password: '',
      confirmPassword: '',
      acceptLic: false,
      isPasswordVisible: false
    }
  },
  methods: {
    async registerSubmit() {
      // получаем данные из формы
      // валидируем данные из формы
      // отправляем данные на сервер
      if (this.acceptLic) {
        const data = {
          email: this.email,
          password: this.password,
          username: this.username
        }
        console.log(data);

        // Попытка аутентификации пользователя
        try {
          const res = await registerUser(data);
          if (res) {
            saveUserData(res, true)
          } else {
            this.showError(); // Показ ошибки при неудачной аутентификации
          }
        } catch (error) {
          this.showError(); // Показ ошибки в случае исключения
        }
      } else {
        this.showError(); // Показ ошибки при невалидных данных
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
});
</script>

<style scoped lang="scss">
@import "../../assets/styles/Registration.scss";
</style>