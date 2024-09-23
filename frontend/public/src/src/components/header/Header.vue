<template>
  <header :class="{ 'justify-content-end': true }" class="header">
    <language-dropdown></language-dropdown>
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
.header {
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  background-color: #f2f2f2;
  height: 50px;
}
</style>
