<template>
  <div class="w-100" :class="{ 'flex-row-reverse': isAuth, 'flex-column': !isAuth }">
    <Header />
    <sidebar v-if="isAuth"></sidebar>
  </div>
  <RouterView />
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
@import url('https://fonts.googleapis.com/css2?family=Inter:ital,opsz,wght@0,14..32,100..900;1,14..32,100..900&display=swap');
@import 'node_modules/vue-multiselect/dist/vue-multiselect';
@import 'node_modules/vue-multiselect-bootstrap-theme/dist/vue-multiselect-bootstrap5';
</style>
