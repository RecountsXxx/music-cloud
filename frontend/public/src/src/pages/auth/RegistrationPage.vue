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
            @input="validation.username.$touch()"
            @blur="validation.username.$touch()"
        />
        <span v-if="validation.username.$error">
          <div class="Errors__Message"
               v-for="(error) in usernameErrors()"
          >{{ error }}</div>
        </span>
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
import {ref} from "vue";
import {helpers, maxLength, minLength, required} from "@vuelidate/validators";
import useVuelidate from "@vuelidate/core";
import {useI18n} from "vue-i18n";

export default {
  setup() {

    const {t} = useI18n();
    let uniqueUsername = ref(false);

    const username = ref('');
    const email = ref('');
    const password = ref('');
    const confirmPassword = ref('');
    const acceptLic = ref(false);
    const rules = {
      username: {
        required,
        minLength: minLength(3),
        maxLength: maxLength(15),
        regex: helpers.regex(/^[a-zA-Z][a-zA-Z0-9_-]*$/),
        uniqueUsername
      },
      uniqueUsername: {
        isUnique: value => !value
      }
    }

    const validation = useVuelidate(rules, {username, uniqueUsername});

    function usernameErrors() {
      const errors = []
      if (this.validation.username.required.$invalid) {
        errors.push(t('RegisterForm.Errors.username.Required'))
      }
      if (this.validation.username.minLength.$invalid) {
        errors.push(t('RegisterForm.Errors.username.MinLength'))
      }
      if (this.validation.username.maxLength.$invalid) {
        errors.push(t('RegisterForm.Errors.username.MaxLength'))
      }
      if (this.validation.username.regex.$invalid) {
        errors.push(t('RegisterForm.Errors.username.Regex'))
      }
      console.log(this.validation.uniqueUsername.$invalid)
      if (this.validation.uniqueUsername.$invalid) {
        console.log('work')
        errors.push(t('RegisterForm.Errors.username.uniqueUsername'))
      }

      return errors;
    }

    function registerSubmit() {
      uniqueUsername.value = true;
      validation.username.$touch()
      // СДЕЛАТЬ ВЫЗОВ ВАЛАДИАЦИИ ЧТО БЫ ВЫВЕСТИ ОШИБКУ

      // if (!validation.value.$invalid) {
      //   alert('Form submitted!');
      // }
    }

    return {
      username,
      validation,
      usernameErrors,
      registerSubmit,
      email,
      password,
      confirmPassword,
      acceptLic,
      uniqueUsername
    }
  }
}
</script>

<style scoped lang="scss">
@import '../../assets/styles/auth/Registration';
</style>
