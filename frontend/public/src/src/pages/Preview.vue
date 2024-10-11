<template>
  <div class="main-container">
    <div class="main-content">
      <div class="m-content">
        <Header />
        <div class="preview-title">{{ $t('PreviewPage.title') }}</div>
        <div class="preview-text">{{ $t('PreviewPage.text') }}</div>
        <div class="preview-info">{{ $t('PreviewPage.info') }}</div>
        <router-link class="link-preview" :to="{ name: 'Register' }"
        >{{ $t('PreviewPage.linkRegister') }}
        </router-link>
      </div>
    </div>
    <div class="content-wrapper">
      <div class="content">
        <div class="recommended">
          <div class="title">{{ $t('PreviewPage.Recommended.title') }}</div>
          <div class="text">{{ $t('PreviewPage.Recommended.text') }}</div>
          <div class="avatars">
            <div class="avatar">
              <img src="@/assets/images/preview/recommended/img1.jpg"
                   alt="Avatar 1" />
            </div>
            <div class="avatar">
              <img src="@/assets/images/preview/recommended/img2.jpg"
                   alt="Avatar 2" />
            </div>
            <div class="avatar">
              <img src="@/assets/images/preview/recommended/img3.jpg"
                   alt="Avatar 3" />
            </div>
            <div class="avatar">
              <img src="@/assets/images/preview/recommended/img4.jpg"
                   alt="Avatar 4" />
            </div>
          </div>
        </div>
      </div>
      <div class="content">
        <div class="playlist-wrapper">
          <div class="info">
            <div class="title">{{ $t('PreviewPage.Playlists.title') }}</div>
            <div class="text">{{ $t('PreviewPage.Playlists.text') }}</div>
            <router-link class="button" :to="{ name: 'Register' }"
            >{{ $t('PreviewPage.Playlists.buttonCreate') }}
            </router-link>
          </div>

          <div class="playlists">
            <div class="playlist">
              <img src="@/assets/images/preview/playlists/hit1.jpg"
                   alt="Piano Meditation" />
            </div>
            <div class="playlist">
              <img src="@/assets/images/preview/playlists/hit2.jpg"
                   alt="Hot Summer Hits" />
            </div>
            <div class="playlist">
              <img src="@/assets/images/preview/playlists/hit3.jpg"
                   alt="Dance Hits" />
            </div>
          </div>
        </div>
      </div>

      <div class="content">
        <div class="category">
          <div class="title">{{ $t('PreviewPage.Category.title') }}</div>
          <div class="text">{{ $t('PreviewPage.Category.text') }}</div>
          <div class="images-container">
            <div class="item">
              <img src="@/assets/images/preview/categories/hiphop.jpg">
              <div class="name">{{ $t('PreviewPage.Category.items.HipHop') }}
              </div>
            </div>
            <div class="item">
              <img src="@/assets/images/preview/categories/Country.png">
              <div class="name">{{ $t('PreviewPage.Category.items.Country') }}
              </div>
            </div>
            <div class="item">
              <img src="@/assets/images/preview/categories/Disco.jpg">
              <div class="name">{{ $t('PreviewPage.Category.items.Disco') }}
              </div>
            </div>
            <div class="item">
              <img src="@/assets/images/preview/categories/Rock.jpg">
              <div class="name">{{ $t('PreviewPage.Category.items.Rock') }}
              </div>
            </div>
            <div class="item">
              <img src="@/assets/images/preview/categories/Reggaeton.jpg">
              <div class="name">{{ $t('PreviewPage.Category.items.Reggaeton')
                }}
              </div>
            </div>
            <div class="item">
              <img src="@/assets/images/preview/categories/Indie.jpg">
              <div class="name">{{ $t('PreviewPage.Category.items.Indie') }}
              </div>
            </div>
            <div class="item">
              <img src="@/assets/images/preview/categories/Classical.jpg">
              <div class="name">{{ $t('PreviewPage.Category.items.Classical')
                }}
              </div>
            </div>
            <div class="item">
              <img src="@/assets/images/preview/categories/Techno.jpg">
              <div class="name">{{ $t('PreviewPage.Category.items.Techno') }}
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="content">
        <div class="search">

          <div class="images-container">
            <div class="image">
              <img src="@/assets/images/preview/search/1.jpg" alt="">
            </div>
            <div class="image">
              <img src="@/assets/images/preview/search/2.jpg" alt="">
            </div>
            <div class="image">
              <img src="@/assets/images/preview/search/3.jpg" alt="">
            </div>
          </div>
          <div class="info">
            <div class="title">{{ $t('PreviewPage.Search.title') }}</div>
            <div class="text">{{ $t('PreviewPage.Search.text') }}</div>
            <a class="buttonToSearch"
               href="#search">{{ $t('PreviewPage.Search.goToSearch') }}</a>
          </div>
        </div>
      </div>
      <div class="content">
        <div class="contact">
          <div class="title"></div>
          <div class="text"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="js">
import Header from '@/components/header/Header.vue'
</script>

<style lang="scss">
@import '@/assets/styles/preview/Preview';
</style>
