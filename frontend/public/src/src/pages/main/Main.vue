<template>
  <div class="main-container">
    <SideBar/>
    <div class="main-content">
      <Header/>
    </div>
  </div>
  <Footer/>
</template>

<script setup>
import SideBar from '@/components/sidebar/Sidebar.vue'
import Header from '@/components/header/Header.vue'
import Footer from '@/pages/Footer.vue'
</script>

<style lang="scss">
@import "@/assets/mixin/_mixins";

.login-button {
  @include button-styles(50px, linear-gradient(180deg, #dc7aef -52.56%, #9282ff 100%));
}

.register-button {
  @include button-styles(30px, none, $border-register-b);
}

.main-container {
  width: 100%;
  height: auto;
  display: flex;
  flex-direction: row;

  .main-content {
    width: 100%;
    height: 100%;
    background: #120E15;
  }
}

.search-container {
  background-color: #291f32;

  .search {
    &::placeholder {
      color: #685A74;
    }
  }
}
</style>
