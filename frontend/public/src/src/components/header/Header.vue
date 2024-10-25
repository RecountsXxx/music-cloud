<template>
  <header id="header">
    <Logo v-if="route.name === 'Preview'" />
    <header-navigation />
    <Search />
    <HeaderControls />
  </header>
</template>

<script lang="js">
import { useRoute } from 'vue-router';
import { useAuthStore } from '@/stores/authStore.js';
import HeaderNavigation from '@/components/header/HeaderNavigation.vue';
import HeaderControls from '@/components/header/HeaderControls.vue';
import Search from '@/components/header/Search.vue';
import Logo from '@/components/logo/Logo.vue';

export default {
  name: 'Header',
  methods: { useAuthStore },
  components: { HeaderControls, Logo, Search, HeaderNavigation },
  setup() {
    const route = useRoute();

    return {
      route,
    };
  },
};
</script>

<style scoped lang="scss">
@import "@/assets/styles/header/Header.scss";
</style>
