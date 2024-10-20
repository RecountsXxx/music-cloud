<template>
  <div class="main-container">
    <SideBar/>
    <div class="main-content">
      <TopHeader/>
      <div class="content"></div>
    </div>
  </div>
  <Footer/>
</template>

<script setup>
import TopHeader from '@/components/header/topHeader.vue'
import Footer from '@/pages/Footer.vue'
import SideBar from '@/components/sidebar/Sidebar.vue'
</script>

<style lang="scss">
.main-container {
  width: 100%;
  height: 100%;
  min-width: 850px;

  display: flex;
  flex-direction: row;

  .main-content {
    width: 100%;
    height: 100%;
    background-color: #120E15;

  }
}
</style>
