<template>
  <div :class="{ 'flex-row-reverse': isAuth, 'flex-column': !isAuth }" class="main-container">
    <Header />
    <sidebar v-if="isAuth"></sidebar>
    <RouterView />
  </div>
</template>

<script lang="js">
import { computed, defineComponent, ref } from 'vue'
import Header from '@/components/header/Header.vue'
import Sidebar from '@/components/sidebar/Sidebar.vue'
import { useAuthStore } from '@/stores/authStore.js'

export default defineComponent({
  components: { Sidebar, Header },
  setup() {
    const authStore = useAuthStore()
    const isAuth = computed(() => authStore.getIsAuthenticated)

    return {
      isAuth
    }
  }
})
</script>

<style lang="scss">
@import '@/assets/styles/resetCSS.css';
@import '@/assets/styles/App.scss';
@import 'node_modules/vue-multiselect/dist/vue-multiselect';
@import 'node_modules/vue-multiselect-bootstrap-theme/dist/vue-multiselect-bootstrap5';
</style>
