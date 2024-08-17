<template>
  <MainHeader/>
  <RouterView />
</template>

<script lang="js">
import {defineComponent} from "vue";
import MainHeader from "./components/MainHeader.vue";

export default defineComponent({
  components: {MainHeader},
  setup() {
    return {}
  }
});
</script>

<style lang="scss">
@import "assets/styles/resetCSS.css";
@import "assets/styles/App.scss";
@import "node_modules/vue-multiselect/dist/vue-multiselect";
@import "node_modules/vue-multiselect-bootstrap-theme/dist/vue-multiselect-bootstrap5";
</style>
