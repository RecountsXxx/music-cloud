<template>
    <Header/>
  <router-view/>
</template>

<script lang="js">
import {defineComponent} from "vue";
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.bundle.min.js'
import Header from "./components/Header.vue";

export default defineComponent({
  components: {Header},
  setup() {
    return {}
  }
});
</script>

<style lang="scss">
@import "styles/resetCSS.css";
@import "styles/App.scss";
</style>