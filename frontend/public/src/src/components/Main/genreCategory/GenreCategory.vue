<template>
  <section class="genre__category">
    <div class="d-flex">
      <div class="genre__category__title">{{ $t('GenreCategory.title') }}</div>
      <router-link to="#" class="genre__show__all">Показать всё</router-link>
    </div>

    <div class="genres__list">
      <GenreItem v-for="item in items"
                 :key="item.name"
                 :name="item.name"
                 :path="item.path" />
    </div>
  </section>
</template>

<script>
import GenreItem from '@/components/Main/genreCategory/GenreItem.vue'
import { useI18n } from 'vue-i18n'

export default {
  name: 'GenreCategory',
  components: { GenreItem },
  setup() {
    const { t } = useI18n()
    const items = [
      {
        name: t('PreviewPage.genres.items.hiphop'),
        path: new URL('@/assets/images/preview/genres/hiphop.jpg', import.meta.url).href
      },
      {
        name: t('PreviewPage.genres.items.hiphop'),
        path: new URL('@/assets/images/preview/genres/hiphop.jpg', import.meta.url).href // замените путь на реальный путь к изображению
      }
    ]
    return {
      items
    }
  }
}
</script>

<style lang="scss">
.genre__category {
  width: 100%;
  display: flex;
  flex-direction: column;
}
</style>
