<template>
  <header id="header">
    <Logo v-if="route.name === 'Preview'" />
    <Search />
    <router-link v-if="route.name === 'Preview'" class="link-main" :to="{name : 'Main'}">Главная</router-link>
    <language-dropdown />
    <Upload />
    <notification v-if="useAuthStore().getIsAuthenticated" />
    <div v-if="!useAuthStore().getIsAuthenticated" class="header__auth">
      <router-link :to="{ name: 'Register' }"
                   class="auth__button auth__button--register">Регистрация
      </router-link>
      <router-link :to="{ name: 'Login' }"
                   class="auth__button auth__button--login">Вход
      </router-link>
    </div>
  </header>
</template>

<script lang="js">
import Logo from '@/components/logo/Logo.vue'
import LanguageDropdown
  from '@/components/languageSelect/LanguageSelect.vue'
import Search from '@/components/header/Search.vue'
import { useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/authStore.js'
import Upload from '@/components/upload/Upload.vue'
import Notification from '@/components/notification/Notification.vue'

export default {
  name: 'topHeader',
  methods: { useAuthStore },
  components: { Logo, Notification, Upload, Search, LanguageDropdown },
  setup() {
    const route = useRoute()

    return {
      route
    }
  }
}
</script>

<style scoped lang="scss">
@import '@/assets/styles/topHeader/TopHeader.scss';
</style>
