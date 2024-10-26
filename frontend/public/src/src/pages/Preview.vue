<template>
  <div class="main__container">
    <section class="hero">
      <div class="blade__filer">
        <Header />
        <div class="hero__content">
          <h1 class="hero__title">{{ $t('PreviewPage.hero.title') }}</h1>
          <p class="hero__description">{{ $t('PreviewPage.hero.description') }}</p>
          <router-link :to="{ name: 'Register' }" class="hero__btn">
            {{ $t('PreviewPage.hero.button') }}
          </router-link>
        </div>
      </div>
    </section>
    <div class="sections__container">
      <!-- Рекомендации -->
      <section class="recommendations wave">
        <div class="wave"></div>
        <h2 class="recommendations__title">
          {{ $t('PreviewPage.recommendations.title') }}
        </h2>
        <p class="recommendations__description">
          {{ $t('PreviewPage.recommendations.description') }}
        </p>
        <div class="recommendations__list">
          <div class="recommendations__item">
            <img
              src="@/assets/images/preview/recommended/img3.jpg"
              alt="Artist 1"
              class="recommendations__image"
            />
          </div>
          <div class="recommendations__item">
            <img
              src="@/assets/images/preview/recommended/img2.jpg"
              alt="Artist 2"
              class="recommendations__image"
            />
          </div>
          <div class="recommendations__item">
            <img
              src="@/assets/images/preview/recommended/img1.jpg"
              alt="Artist 3"
              class="recommendations__image"
            />
          </div>
          <div class="recommendations__item">
            <img
              src="@/assets/images/preview/recommended/img4.jpg"
              alt="Artist 4"
              class="recommendations__image"
            />
          </div>
        </div>
      </section>
      <section class="playlists wave">
        <div class="text__content">
          <h2 class="playlists__title">
            {{ $t('PreviewPage.playlists.title') }}
          </h2>
          <div class="playlists__description">
            {{ $t('PreviewPage.playlists.description') }}
          </div>
          <router-link to="#" class="playlists__btn">
            {{ $t('PreviewPage.playlists.button') }}
          </router-link>
        </div>
        <div class="playlists__images">
          <div class="playlists__item">
            <img
              src="@/assets/images/preview/playlists/hit2.jpg"
              alt="Hot Summer Hits"
              class="playlists__image"
            />
          </div>
          <div class="playlists__item">
            <img
              src="@/assets/images/preview/playlists/hit3.jpg"
              alt="Dance Hits"
              class="playlists__image"
            />
          </div>
          <div class="playlists__item">
            <img
              src="@/assets/images/preview/playlists/hit1.jpg"
              alt="Piano Meditation"
              class="playlists__image"
            />
          </div>
        </div>
      </section>
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

      <!-- Поиск -->
      <section class="search wave">
        <div class="search__text__content">
          <h1 class="search__title">{{ $t('PreviewPage.search.title') }}</h1>
          <p class="search__description">{{ $t('PreviewPage.search.description') }}</p>
          <a href="#search" class="search__btn">{{ $t('PreviewPage.search.button') }}</a>
        </div>
        <div class="search__images">
          <img src="@/assets/images/preview/search/1.jpg" alt="img1" class="search__image" />
          <img src="@/assets/images/preview/search/2.jpg" alt="img2" class="search__image" />
          <img src="@/assets/images/preview/search/3.jpg" alt="img3" class="search__image" />
        </div>
      </section>

      <!-- Аккаунт -->
      <section class="account">
        <h2 class="account__title">{{ $t('PreviewPage.account.title') }}</h2>
        <p class="account__description">{{ $t('PreviewPage.account.description') }}</p>
        <div class="account__list">
          <div class="account__item">
            <img src="@/assets/images/preview/account/1.png" alt="User 1" class="account__image" />
          </div>
          <div class="account__item">
            <img src="@/assets/images/preview/account/2.png" alt="User 2" class="account__image" />
          </div>
          <div class="account__item">
            <img src="@/assets/images/preview/account/3.png" alt="User 3" class="account__image" />
          </div>
          <div class="account__item">
            <img src="@/assets/images/preview/account/4.png" alt="User 4" class="account__image" />
          </div>
          <div class="account__item">
            <img src="@/assets/images/preview/account/5.png" alt="User 5" class="account__image" />
          </div>
          <div class="account__item">
            <img src="@/assets/images/preview/account/6.png" alt="User 6" class="account__image" />
          </div>
        </div>
        <router-link :to="{ name: 'Register' }" class="account__btn">
          {{ $t('PreviewPage.account.button') }}
        </router-link>
      </section>
    </div>
    <Footer />
  </div>
</template>

<script setup lang="js">
import Header from '@/components/header/Header.vue'
import Footer from '@/pages/Footer.vue'
</script>

<style lang="scss">
@import '@/assets/styles/preview/Preview';
</style>
