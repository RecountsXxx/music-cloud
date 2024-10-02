<template>
  <header class="header">
    <search />
    <router-link v-if="!isAuth" class="link-main" to="/">{{ $t('header.title') }}</router-link>
    <language-dropdown />
    <div v-if="!isAuth">
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
import LanguageDropdown from '@/components/languageDropdown/LanguageDropdown.vue'
import { useAuthStore } from '@/stores/authStore.js'
import { computed, watch } from 'vue'
import Search from '@/components/header/Search.vue'

export default {
  name: 'Header',
  components: { Search, LanguageDropdown },
  setup() {
    const authStore = useAuthStore()
    const isAuth = computed(() => authStore.getIsAuthenticated)

    watch(isAuth, (newIsAuth) => {})

    return {
      isAuth
    }
  }
}
</script>

<style scoped lang="scss">
@import '@/assets/styles/header/Header';
@import '@/assets/styles/header/state/noAuth';
</style>
