<template>
  <section class="recommended-artists">
    <div
      v-for="artist in artists"
      :key="artist.id"
      class="recommended-artists__item"
    >
      <img :src="artist.image" alt="" class="recommended-artists__image" />
      <p class="recommended-artists__name">{{ artist.name }}</p>
      <p class="recommended-artists__followers">{{ artist.followers }} подписчиков</p>
    </div>
  </section>
</template>

<script>
export default {
  name: 'RecommendedArtists',
  props: {
    artists: Array,
  },
};
</script>

<style scoped>
.recommended-artists {
  /* Стили по методологии БЭМ */
}
.recommended-artists__item {
  /* Стили по методологии БЭМ */
}
.recommended-artists__image {
  /* Стили по методологии БЭМ */
}
.recommended-artists__name {
  /* Стили по методологии БЭМ */
}
.recommended-artists__followers {
  /* Стили по методологии БЭМ */
}
</style>
