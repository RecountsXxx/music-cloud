<template>
  <!--  <router-link to="#" class="genres__item">-->
  <!--    <img class="genres__image"-->
  <!--         :src="path" :alt="path" />-->
  <!--    <div class="genres__color__fade"></div>-->
  <!--    <div class="genres__name">{{ name }}</div>-->
  <!--  </router-link>-->

</template>

<script>
export default {
  name: 'GenreItem',
  props: {
    name: {
      type: String,
      required: true
    },
    path: {
      type: String,
      required: true
    }
  },
  setup() {

  }
}
</script>

<style scoped lang="scss">

</style>