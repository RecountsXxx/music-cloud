<template>
  <header class="header">
    <Search/>
    <router-link :to="{ name: 'Preview' }" v-if="route.name==='Register'">
      Главная
    </router-link>
    <language-dropdown/>
    <div v-if="(route.name==='Preview' || route.name === 'Main') && !useAuthStore().getIsAuthenticated">
      <router-link :to="{ name: 'Register' }" class="register-button"
      >{{ $t('header.buttonRegistration') }}
      </router-link>
      <router-link :to="{ name: 'Login' }" class="login-button"
      >{{ $t('header.buttonLogin') }}
      </router-link>
    </div>
  </header>
</template>

<script lang="js">
import LanguageDropdown
  from '@/components/languageDropdown/LanguageDropdown.vue'
import Search from '@/components/header/Search.vue'
import {useRoute} from 'vue-router'
import {useAuthStore} from '@/stores/authStore.js'

export default {
  name: 'Header',
  methods: {useAuthStore},
  components: {Search, LanguageDropdown},
  setup() {
    const route = useRoute()

    return {
      route
    }
  }
}
</script>

<style scoped lang="scss">
@import '@/assets/styles/header/Header';
</style>
