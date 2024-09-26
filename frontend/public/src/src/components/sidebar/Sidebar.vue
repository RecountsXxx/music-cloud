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
        <router-link to="#" class="playlist-item">
          
        </router-link>
      </div>
      <ul>
        <li><a href="#">Любимые треки</a></li>
        <li><a href="#">Плейлист 1</a></li>
        <li><a href="#">Плейлист 2</a></li>
      </ul>
    </div>
  </aside>
</template>

<script>
export default {
  name: 'Sidebar'
}
</script>

<style scoped lang="scss">
@import '@/assets/styles/home/Sidebar.scss';
</style>
