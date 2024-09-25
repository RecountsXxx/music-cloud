<template>
    <header :class="{ 'justify-content-end': true }" class="header">
<!--  <header class="header">-->
    <language-dropdown/>
  </header>
</template>

<script>
import LanguageDropdown from '@/components/languageDropdown/LanguageDropdown.vue'

export default {
  name: 'Header',
  components: { LanguageDropdown }
}
</script>

<style scoped lang="scss">
@import "@/assets/styles/header/Header.scss";
</style>
