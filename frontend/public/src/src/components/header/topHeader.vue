<template>
  <header id="header">

  </header>
</template>

<script lang="js">
import LanguageDropdown
  from '@/components/languageDropdown/LanguageDropdown.vue'
import Search from '@/components/header/Search.vue'
import {useRoute} from 'vue-router'
import {useAuthStore} from '@/stores/authStore.js'

export default {
  name: 'topHeader',
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
//$background-color: #120E15;
$background-color: red;

#header {
  display: flex;
  flex-direction: row;
  height: 100px;
  width: 100%;

  background: $background-color;
}


@import '@/assets/styles/topHeader/topHeader.scss';
</style>
