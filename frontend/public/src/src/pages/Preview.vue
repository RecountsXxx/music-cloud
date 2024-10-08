<template>
  <div class="main-container">
    <div class="main-content">
      <div class="m-content">
        <Header/>
        <div class="preview-title">{{ $t('PreviewPage.title') }}</div>
        <div class="preview-text">{{ $t('PreviewPage.text') }}</div>
        <div class="preview-info">{{ $t('PreviewPage.info') }}</div>
        <router-link class="link-preview" :to="{ name: 'Register' }">{{ $t('PreviewPage.linkRegister') }}</router-link>
      </div>
    </div>
    <div class="content-wrapper">
      <div class="content">
        <div class="recommended">
          <div class="title">{{ $t('PreviewPage.Recommended.title') }}</div>
          <div class="text">{{ $t('PreviewPage.Recommended.text') }}</div>
          <div class="avatars">
            <div class="avatar">
              <img src="@/assets/images/preview/recommended/img1.jpg" alt="Avatar 1">
            </div>
            <div class="avatar">
              <img src="@/assets/images/preview/recommended/img2.jpg" alt="Avatar 2">
            </div>
            <div class="avatar">
              <img src="@/assets/images/preview/recommended/img3.jpg" alt="Avatar 3">
            </div>
            <div class="avatar">
              <img src="@/assets/images/preview/recommended/img4.jpg" alt="Avatar 4">
            </div>
          </div>
        </div>
      </div>
      <div class="content">
        <div class="playlist-wrapper">
          <div class="info">
            <div class="title">{{ $t('PreviewPage.Playlists.title') }}</div>
            <div class="text">{{ $t('PreviewPage.Playlists.text') }}</div>
            <router-link :to="{ name: 'Register' }">{{ $t('PreviewPage.Playlists.buttonCreate') }}</router-link>
          </div>

          <div class="playlists">
            <div class="playlist">
              <img src="@/assets/images/preview/playlists/hit1.jpg" alt="Piano Meditation">
            </div>
            <div class="playlist">
              <img src="@/assets/images/preview/playlists/hit2.jpg" alt="Hot Summer Hits">
            </div>
            <div class="playlist">
              <img src="@/assets/images/preview/playlists/hit3.jpg" alt="Dance Hits">
            </div>
          </div>
        </div>
      </div>
      <div class="content"></div>
    </div>
  </div>
</template>

<script setup>
import Header from "@/components/header/Header.vue";
</script>

<style lang="scss">
@import '@/assets/styles/preview/Preview';
</style>