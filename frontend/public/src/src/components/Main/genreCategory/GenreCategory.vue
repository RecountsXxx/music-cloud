<!-- Жанры и категории -->
<section class="genres wave">
<div class="genres__title">{{ $t('PreviewPage.genres.title') }}</div>
<div class="genres__description">{{ $t('PreviewPage.genres.description') }}</div>
<div class="genres__list">
  <div class="genres__item">
    <img class="genres__image" src="@/assets/images/preview/genres/hiphop.jpg" />
    <div class="genres__color__fade"></div>
    <div class="genres__name">{{ $t('PreviewPage.genres.items.hiphop') }}</div>
  </div>
  <div class="genres__item">
    <img class="genres__image" src="@/assets/images/preview/genres/Country.png" />
    <div class="genres__color__fade"></div>
    <div class="genres__name">{{ $t('PreviewPage.genres.items.country') }}</div>
  </div>
  <div class="genres__item">
    <img class="genres__image" src="@/assets/images/preview/genres/Disco.jpg" />
    <div class="genres__color__fade"></div>
    <div class="genres__name">{{ $t('PreviewPage.genres.items.disco') }}</div>
  </div>
  <div class="genres__item">
    <img class="genres__image" src="@/assets/images/preview/genres/Rock.jpg" />
    <div class="genres__color__fade"></div>
    <div class="genres__name">{{ $t('PreviewPage.genres.items.rock') }}</div>
  </div>
  <div class="genres__item">
    <img class="genres__image" src="@/assets/images/preview/genres/Reggaeton.jpg" />
    <div class="genres__color__fade"></div>
    <div class="genres__name">
      {{ $t('PreviewPage.genres.items.reggaeton') }}
    </div>
  </div>
  <div class="genres__item">
    <img class="genres__image" src="@/assets/images/preview/genres/Indie.jpg" />
    <div class="genres__color__fade"></div>
    <div class="genres__name">{{ $t('PreviewPage.genres.items.indie') }}</div>
  </div>
  <div class="genres__item">
    <img class="genres__image" src="@/assets/images/preview/genres/Classical.jpg" />
    <div class="genres__color__fade"></div>
    <div class="genres__name">
      {{ $t('PreviewPage.genres.items.classical') }}
    </div>
  </div>
  <div class="genres__item">
    <img class="genres__image" src="@/assets/images/preview/genres/Techno.jpg" />
    <div class="genres__color__fade"></div>
    <div class="genres__name">{{ $t('PreviewPage.genres.items.techno') }}</div>
  </div>
</div>
</section>


<template>
  <section class="genre__category">
    <div class="genre__category__title">{{ $t('GenreCategory.title') }}</div>
    <div class="genre__show__all"></div>

    <GenreItem v-for="item in items"
               :key="item.name"
               :name="item.name"
               :path="item.path" />
  </section>
</template>

<script>
import GenreItem from '@/components/Main/genreCategory/GenreItem.vue'

export default {
  name: 'GenreCategory',
  components: { GenreItem },
  setup() {
    const items = [
      {
        name: 'GenreItem',
        path: '/genre-category/GenreItem'
      },
      {
        name: 'GenreItem',
        path: '/genre-category/GenreItem'
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
  background-color: red;
}

.genre-category__item {
}

.genre-category__button {
  /* Стили по методологии БЭМ */
}
</style>
