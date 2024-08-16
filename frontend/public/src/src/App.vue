<template>
  <MainHeader/>
  <RouterView />
</template>

<script lang="js">
import {defineComponent} from "vue";
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.bundle.min.js'
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
</style>
