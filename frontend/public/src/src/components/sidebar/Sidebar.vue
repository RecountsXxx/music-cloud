<template>
  <aside class="sidebar">
    <div class="title_wrapper">
      <div class="title">Vibe Cloud</div>
    </div>
    <nav class="aside-nav-panel">
      <router-link to="#" class="nav-item">
        <img src="@/assets/images/sidebar/main.svg" alt="" />
        <div class="nav-panel-item-text">Главная</div>
      </router-link>
      <router-link to="#" class="nav-item">
        <img src="@/assets/images/sidebar/investigate.svg" alt="" />
        <div class="nav-panel-item-text">Исследуйте</div>
      </router-link>
      <router-link to="#" class="nav-item">
        <img src="@/assets/images/sidebar/library.svg" alt="" />
        <div class="nav-panel-item-text">Библиотека</div>
      </router-link>
    </nav>

    <div class="playlists-wrapper">
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
            <img src="@/assets/images/sidebar/playlist/Like.svg" alt="Like" />
          </div>h
          <div class="playlist-item-info">Любимые треки</div>
        </router-link>

        <router-link to="/playlist/item1231233" class="playlist-item">
          <div class="img-wrapper">
            <img src="@/assets/images/TEMP/266291769056b15b6cf60ffbbf1b079f.jpg" />
          </div>
          <div class="playlist-item-info">
            <div class="playlist-name">Dance hits</div>
            <router-link to="#" class="playlist-author">Алёна Алёна</router-link>
          </div>
        </router-link>

        <router-link to="/playlist/item1231233" class="playlist-item">
          <div class="img-wrapper">
            <img src="@/assets/images/TEMP/853f34d66e987340a9c701313fd0e4fe.jpg" />
          </div>
          <div class="playlist-item-info">
            <div class="playlist-name">Test</div>
            <router-link to="#" class="playlist-author">Renzo</router-link>
          </div>
        </router-link>
      </div>
    </div>
  </aside>
</template>

<script>
export default {
  name: 'SideBar'
}
</script>

<style scoped lang="scss">
@import '@/assets/styles/home/Sidebar.scss';
</style>
