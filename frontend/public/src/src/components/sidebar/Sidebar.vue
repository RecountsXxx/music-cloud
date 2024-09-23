<template>
  <aside class="sidebar">
    <nav>
      <ul>
        <li>
          <router-link to="#">Главная</router-link>
        </li>
        <li>
          <router-link to="#">Исследуйте</router-link>
        </li>
        <li>
          <router-link to="#">Библиотека</router-link>
        </li>
      </ul>
    </nav>

    <div class="playlists">
      <h3>Плейлисты</h3>
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
@import '@/assets/styles/home/sidebar.scss';
</style>
