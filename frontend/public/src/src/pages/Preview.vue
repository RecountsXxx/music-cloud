<template>
  <div class="main__container">
    <section class="hero">
      <div class="blade__filer">
        <Header />
        <div class="hero__content">
          <h1 class="hero__title">{{ $t('PreviewPage.hero.title') }}</h1>
          <p class="hero__description">{{ $t('PreviewPage.hero.description')
            }}</p>
          <router-link :to="{ name: 'Register'}" class="hero__btn">
            {{ $t('PreviewPage.hero.button') }}
          </router-link>
        </div>
      </div>
    </section>
    <!-- Рекомендации -->
    <section class="recommendations custom__section">
      <div class="wave"></div>
      <h2 class="recommendations__title">
        {{ $t('PreviewPage.recommendations.title') }}</h2>
      <p class="recommendations__description">
        {{ $t('PreviewPage.recommendations.description') }}</p>
      <div class="recommendations__list">
        <div class="recommendations__item">
          <img src="@/assets/images/preview/recommended/img3.jpg"
               alt="Artist 1"
               class="recommendations__image">
        </div>
        <div class="recommendations__item">
          <img src="@/assets/images/preview/recommended/img2.jpg"
               alt="Artist 2"
               class="recommendations__image">
        </div>
        <div class="recommendations__item">
          <img src="@/assets/images/preview/recommended/img1.jpg"
               alt="Artist 3"
               class="recommendations__image">
        </div>
        <div class="recommendations__item">
          <img src="@/assets/images/preview/recommended/img4.jpg"
               alt="Artist 4"
               class="recommendations__image">
        </div>
      </div>
    </section>
    <Footer />
  </div>
</template>

<script setup lang="js">
import Header from '@/components/header/Header.vue';
import Footer from '@/pages/Footer.vue';
</script>

<style lang="scss">
@import '@/assets/styles/preview/Preview';
</style>
