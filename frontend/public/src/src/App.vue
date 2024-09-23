<template>
  <Header></Header>
  <RouterView />
</template>

<script lang="js">
import { defineComponent } from 'vue'
import Header from '@/components/header/Header.vue'

export default defineComponent({
  components: { Header },
  setup() {
    return {}
  }
})
</script>

<style lang="scss">
@import '@/assets/styles/resetCSS.css';
@import '@/assets/styles/App.scss';
@import 'node_modules/vue-multiselect/dist/vue-multiselect';
@import 'node_modules/vue-multiselect-bootstrap-theme/dist/vue-multiselect-bootstrap5';
</style>
