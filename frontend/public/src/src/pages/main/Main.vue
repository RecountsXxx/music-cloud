<template>
  <div class="main-container">
    <SideBar />
    <div class="main-content">
      <TopHeader />
      <div class="content">
        <GenreCategory />
      </div>
    </div>
  </div>
  <Footer />
</template>

<script>
import { defineComponent } from 'vue'
import TopHeader from '@/components/header/Header.vue'
import Footer from '@/pages/Footer.vue'
import SideBar from '@/components/sidebar/Sidebar.vue'
import GenreCategory from '@/components/Main/genreCategory/GenreCategory.vue'

export default defineComponent({
  components: { GenreCategory, SideBar, TopHeader, Footer },
  setup() {
    return {}
  }
})
</script>

<style lang="scss">
@import '@/assets/styles/main/Main.scss';
</style>
