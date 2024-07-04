<template>
  <router-view/>
</template>

<script lang="js">
import {defineComponent} from "vue";
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.bundle.min.js'

export default defineComponent({
  components: {},
  setup() {
    return {}
  }
});
// import './styles/resetCSS.css'
</script>

<style lang="css">
:root {
  --main-background-color: linear-gradient(to bottom, #131313, #000000)
}

html, body {
  width: 100%;
  height: 100%;
  margin: 0;
  padding: 0;
}

body, #app {
  overflow: auto;
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background-image: var(--main-background-color);
}

</style>