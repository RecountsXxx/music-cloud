<template>
  <main id="main__container">
    <div class="form__title">{{ $t("RegisterForm.form__title") }}</div>
    <form id="register__form" @submit.prevent="registerSubmit">
      <!-- Поле ввода для имени пользователя -->
      <input class="form__input" type="text" v-model="username" :placeholder="$t('RegisterForm.placeholder.username')">

      <!-- Поле ввода для email -->
      <input
          class="form__input"
          v-model="email"
          id="email"
          type="text"
          :placeholder="$t('RegisterForm.placeholder.email')"
          @input="clearError"
      />

      <!-- Поле ввода для пароля -->
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
        <div class="image__wrapper">
          <img
              class="showHidePassword"
              src="../../assets/images/showPassword.svg"
              alt=""
              @click="changeVisiblePassword"
          />
        </div>
      </div>

      <!-- Поле ввода для подтверждения пароля -->
      <input class="form__input" type="text" v-model="confirmPassword" :placeholder="$t('RegisterForm.placeholder.confirmPassword')">

      <!-- Чекбокс для принятия условий -->
      <div class="acceptPrivacyPolicy">
        <input id="accept" name="acceptCheckBox" type="checkbox" v-model="acceptLic">
        <label for="accept">
          {{ $t('RegisterForm.accept.IAccept') }}
          <router-link to="#">{{ $t('RegisterForm.accept.terms') }}</router-link>
          {{ $t('RegisterForm.accept.and') }}
          <router-link to="#">{{ $t('RegisterForm.accept.privacy_policy') }}</router-link>
        </label>
      </div>

      <!-- Кнопка отправки формы -->
      <input type="submit" class="submit__button" :value="$t('RegisterForm.buttonSubmit')">

      <!-- Ссылка на страницу входа -->
      <div class="link__login__account">
        {{ $t("RegisterForm.login.haveAcc") }}
        <router-link :to="{ name: 'Login' }">{{ $t("RegisterForm.login.signIn") }}</router-link>
      </div>
    </form>
  </main>
</template>

<script lang="js">
import { defineComponent } from "vue";
import { saveUserData } from "@/utils/saveUserData.js";
import { showHidePassword } from "@/utils/showHidePassword.js";
import { register } from "@/utils/query-system/query-actions/authActions.js";

export default defineComponent({
  data() {
    return {
      isError: false, // Флаг наличия ошибки
      email: "", // Данные email пользователя
      username: "", // Имя пользователя
      password: "", // Пароль пользователя
      confirmPassword: "", // Подтверждение пароля
      acceptLic: false, // Принятие условий использования
      isPasswordVisible: false, // Флаг видимости пароля
    };
  },
  methods: {
    async registerSubmit() {
      // Проверка принятия условий использования
      if (this.acceptLic) {
        const data = {
          email: this.email,
          password: this.password,
          username: this.username,
        };
        console.log(data);

        try {
          const res = await register(data); // Регистрация пользователя
          if (res) {
            saveUserData(res, true); // Сохранение данных пользователя
          } else {
            this.showError(); // Показ ошибки при неудачной регистрации
          }
        } catch (error) {
          this.showError(); // Показ ошибки в случае исключения
        }
      } else {
        this.showError(); // Показ ошибки, если условия не приняты
      }
    },
    changeVisiblePassword() {
      this.isPasswordVisible = showHidePassword(this.$refs.passwordInput); // Переключение видимости пароля
    },
    clearError() {
      // Сброс флага ошибки и скрытие сообщения об ошибке
      if (this.isError) {
        this.isError = false;
        this.$refs.errorMessage.style.visibility = "hidden";
      }
    },
    showError() {
      // Метод для показа сообщения об ошибке
      this.isError = true;
      this.$refs.errorMessage.style.visibility = "visible";
    },
  },
});
</script>

<style scoped lang="scss">
@import "../../assets/styles/auth/Registration";
</style>
