<template>
  <section class="recommended-releases">
    <div
      v-for="release in releases"
      :key="release.id"
      class="recommended-releases__item"
    >
      <img :src="release.image" alt="" class="recommended-releases__image" />
      <p class="recommended-releases__title">{{ release.title }}</p>
      <p class="recommended-releases__artist">{{ release.artist }}</p>
    </div>
  </section>
</template>

<script>
export default {
  name: 'RecommendedReleases',
  props: {
    releases: Array,
  },
};
</script>

<style scoped>
.recommended-releases {
  /* Стили по методологии БЭМ */
}
.recommended-releases__item {
  /* Стили по методологии БЭМ */
}
.recommended-releases__image {
  /* Стили по методологии БЭМ */
}
.recommended-releases__title {
  /* Стили по методологии БЭМ */
}
.recommended-releases__artist {
  /* Стили по методологии БЭМ */
}
</style>
