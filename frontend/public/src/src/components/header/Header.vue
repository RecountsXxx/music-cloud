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
$background-color: #313131;
$border-color: #797979;

.header {
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  background-color: $background-color;
  height: 50px;
  border-bottom: $border-color 1px solid;
}
</style>
