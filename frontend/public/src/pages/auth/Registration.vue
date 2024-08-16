<template>
  <main id="main__container">
    <!-- Заголовок формы -->
    <div class="form__title">{{ $t("RegisterForm.form__title") }}</div>

    <!-- Форма регистрации -->
    <form id="register__form" @submit.prevent="registerSubmit">

      <!-- Поле ввода имени пользователя -->
      <input
          class="form__input"
          type="text"
          :placeholder="$t('RegisterForm.placeholder.username')"
      />

      <!-- Поле ввода email -->
      <input
          class="form__input"
          type="text"
          :placeholder="$t('RegisterForm.placeholder.email')"
      />

      <!-- Поле для пароля с функцией показать/скрыть пароль -->
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
              alt="Показать/скрыть пароль"
              @click="changeVisiblePassword"
          />
        </div>
      </div>

      <!-- Поле для подтверждения пароля -->
      <input
          class="form__input"
          type="text"
          :placeholder="$t('RegisterForm.placeholder.confirmPassword')"
      />

      <!-- Поле для принятия политики конфиденциальности -->
      <div class="acceptPrivacyPolicy">
        <input
            id="accept"
            name="acceptCheckBox"
            type="checkbox"
            v-model="acceptLic"
        />
        <label for="accept">
          {{ $t('RegisterForm.accept.IAccept') }}
          <router-link to="#">
            {{ $t('RegisterForm.accept.terms') }}
          </router-link>
          {{ $t('RegisterForm.accept.and') }}
          <router-link to="#">
            {{ $t('RegisterForm.accept.privacy_policy') }}
          </router-link>
        </label>
      </div>

      <!-- Кнопка отправки формы -->
      <input
          type="submit"
          class="submit__button"
          :value="$t('RegisterForm.buttonSubmit')"
      />

      <!-- Ссылка на вход в аккаунт для уже зарегистрированных пользователей -->
      <div class="link__login__account">
        {{ $t("RegisterForm.login.haveAcc") }}
        <router-link :to="{ name: 'Login' }">
          {{ $t("RegisterForm.login.signIn") }}
        </router-link>
      </div>
    </form>
  </main>
</template>


<script lang="js">
import {defineComponent} from "vue";
import {showHidePassword} from "@/utils/showHidePassword";

export default defineComponent({
  data() {
    return {
      isError: false,
      email: '',
      password: '',
      confirmPassword: '',
      acceptLic: false,
      isPasswordVisible: false
    }
  },
  methods: {
    registerSubmit() {
      // получаем данные из формы
      // валидируем данные из формы
      // отправляем данные на сервер
      if (this.acceptLic) {
        const data = {

        }
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