<template>
  <div class="header__auth__controls">
    <div v-if="!useAuthStore().getIsAuthenticated" class="header__auth">
      <router-link :to="{ name: 'Register' }"
                   class="auth__button auth__button--register">Регистрация
      </router-link>
      <router-link :to="{ name: 'Login' }"
                   class="auth__button auth__button--login">Вход
      </router-link>
    </div>
  </div>
</template>


<script setup>

import { useAuthStore } from '@/stores/authStore.js';
</script>

<style scoped lang="scss">
.header__auth__controls {
  margin-left: 40px;
  width: fit-content;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;

  .header__auth {
    width: fit-content;
    display: flex;
    align-items: center;
    justify-content: center;

    .auth__button {
      color: rgba(247, 247, 247, 1);
      text-decoration: none;
      display: flex;
      align-items: center;
      justify-content: center;
      font-family: Inter, serif;
      font-size: 18px;
      line-height: 21.78px;
      text-align: center;

    }

    .auth__button--login {
      font-weight: 500;

      width: 104px;
      height: 39px;
      padding: 9px 30px;
      border-radius: 5px;
      background: linear-gradient(180deg, #DC7AEF -52.56%, #9282FF 100%);

    }

    .auth__button--register {
      margin-right: 30px;
      font-weight: 400;
      width: 174px;
      height: 39px;
      top: 30px;
      left: 1212px;
      padding: 9px 30px;
      gap: 10px;
      border-radius: 5px;

      border: 1px solid rgba(146, 130, 255, 1);
    }
  }
}
</style>