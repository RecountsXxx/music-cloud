<template>
  <aside class="sidebar">
    <div class="title_wrapper">
      <div class="title">Vibe Stream</div>
    </div>
    <nav class="aside-nav-panel">
      <router-link :to="{ name: 'Main' }"
                   :class="{'active-page': useRoute().name==='Main' }"
                   class="nav-item">
        <svg viewBox="0 0 24 24" fill="none"
             xmlns="http://www.w3.org/2000/svg">
          <path d="M4.5 9V21H19.5V9L12 3L4.5 9Z" stroke-width="2"
                stroke-linecap="round" stroke-linejoin="round"/>
          <path d="M9.5 14.5V21H14.5V14.5H9.5Z" stroke-width="2"
                stroke-linejoin="round"/>
          <path d="M4.5 21H19.5" stroke-width="2" stroke-linecap="round"/>
        </svg>
        <div class="nav-panel-item-text">Главная</div>
      </router-link>
      <router-link to="#" :class="{'active-page': useRoute().name==='#' }"
                   class="nav-item">
        <svg viewBox="0 0 24 24" fill="none"
             xmlns="http://www.w3.org/2000/svg">
          <path d="M12 2V22" stroke-width="2" stroke-linecap="round"/>
          <path d="M17 6V18" stroke-width="2" stroke-linecap="round"/>
          <path d="M2 9V15" stroke-width="2" stroke-linecap="round"/>
          <path d="M22 9V15" stroke-width="2" stroke-linecap="round"/>
          <path d="M7 6V18" stroke-width="2" stroke-linecap="round"/>
        </svg>

        <div class="nav-panel-item-text">Исследуйте</div>
      </router-link>
      <router-link v-if="useAuthStore().getIsAuthenticated" to="#"
                   class="nav-item">
        <svg viewBox="0 0 24 24" fill="none"
             xmlns="http://www.w3.org/2000/svg">
          <path d="M12 9.5H20" stroke-width="2" stroke-linecap="round"
                stroke-linejoin="round"/>
          <path d="M12 5H20" stroke-width="2" stroke-linecap="round"
                stroke-linejoin="round"/>
          <path d="M4 19H20" stroke-width="2" stroke-linecap="round"
                stroke-linejoin="round"/>
          <path d="M4 14H20" stroke-width="2" stroke-linecap="round"
                stroke-linejoin="round"/>
          <path d="M4 5L8 7.5L4 10V5Z" stroke-width="2"
                stroke-linejoin="round"/>
        </svg>

        <div class="nav-panel-item-text">Библиотека</div>
      </router-link>
    </nav>

    <div v-if="useAuthStore().getIsAuthenticated" class="playlists-wrapper">
      <h3 class="playlist-title">
        <div class="title-text">Плейлисты</div>
        <router-link class="add-playlist" to="#">
          <img
              src="@/assets/images/sidebar/playlist/addPlaylist.svg"
              alt="Add"
              title="Добавить плейлист"
          />
        </router-link>
      </h3>
      <div class="playlists">
        <router-link to="/playlist/item1231233" class="like-item playlist-item">
          <div class="img-wrapper">
            <img src="@/assets/images/sidebar/playlist/Like.svg" alt="Like"/>
          </div>
          <div class="playlist-item-info">Любимые треки</div>
        </router-link>

        <router-link to="/playlist/item1231233" class="playlist-item">
          <div class="img-wrapper">
            <img
                src="@/assets/images/TEMP/266291769056b15b6cf60ffbbf1b079f.jpg"/>
          </div>
          <div class="playlist-item-info">
            <div class="playlist-name">Dance hits</div>
            <router-link to="#" class="playlist-author">Алёна Алёна
            </router-link>
          </div>
        </router-link>

        <router-link to="/playlist/item1231233" class="playlist-item">
          <div class="img-wrapper">
            <img
                src="@/assets/images/TEMP/853f34d66e987340a9c701313fd0e4fe.jpg"/>
          </div>
          <div class="playlist-item-info">
            <div class="playlist-name">Test</div>
            <router-link to="#" class="playlist-author">Renzo</router-link>
          </div>
        </router-link>
      </div>
    </div>
    <div class="noAuthSidebar" v-else>
      <div class="login-text">Войдите,что бы создавать плейлисты,делиться треками и
        релизами,оставлять комментарии,получать рекомендации и моногое другое.
      </div>
      <router-link class="buttonLogin" :to="{name: 'Login'}">Вход</router-link>
    </div>
  </aside>
</template>

<script>
import {useAuthStore} from '@/stores/authStore.js'
import {useRoute, useRouter} from 'vue-router'

export default {
  name: 'SideBar',
  methods: {useRoute, useRouter, useAuthStore}
}
</script>

<style scoped lang="scss">
@import '@/assets/styles/home/Sidebar.scss';
</style>
