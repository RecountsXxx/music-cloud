<template>
  <router-link v-if="route.name === 'Preview'" class="link__main" :to="{ name: 'Main' }"
    >{{ $t('header.title') }}
  </router-link>
  <LanguageDropdown />
  <Upload v-if="!(route.name === 'Register' || route.name === 'Login')" />
  <notification
    v-if="
      useAuthStore().getIsAuthenticated && !(route.name === 'Register' || route.name === 'Login')
    "
  />
  <HeaderAuthControls
    v-if="
      !useAuthStore().getIsAuthenticated && !(route.name === 'Register' || route.name === 'Login')
    "
  />
  <div
    v-if="
      useAuthStore().getIsAuthenticated && !(route.name === 'Register' || route.name === 'Login')
    "
  >
    Account
  </div>
</template>

<script lang="js">
import { useAuthStore } from '@/stores/authStore.js'
import Upload from '@/components/upload/Upload.vue'
import Notification from '@/components/notification/Notification.vue'
import LanguageDropdown from '@/components/languageSelect/LanguageSelect.vue'
import HeaderAuthControls from '@/components/header/HeaderAuthControls.vue'
import { useRoute } from 'vue-router'

export default {
  methods: { useAuthStore },
  components: { HeaderAuthControls, Notification, Upload, LanguageDropdown },
  setup() {
    const route = useRoute()
    return {
      route
    }
  }
}
</script>

<style scoped lang="scss">
.link__main {
  margin-left: 15px;
  text-decoration: none;
  font-family: Inter, serif;
  font-size: 18px;
  font-weight: 400;
  line-height: 21.78px;
  text-align: left;
  color: rgba(195, 186, 255, 1);
}
</style>
