<template>
  <div id="header__navigation">
    <!--  prev  -->
    <a @click="this.$router.go(-1)" href="#"
       class="nav__button nav__button--prev">
      <svg width="44" height="44" viewBox="0 0 44 44" fill="none"
           xmlns="http://www.w3.org/2000/svg">
        <circle cx="22" cy="22" r="22" fill="#382D3F" fill-opacity="0.5" />
        <path d="M26 31L17 22.5L26 14" stroke="#715F81" stroke-width="2"
              stroke-linecap="round" stroke-linejoin="round" />
      </svg>
    </a>
    <!--  next  -->
    <a @click="this.$router.go(1)" class="nav__button nav__button--next">
      <svg width="44" height="44" viewBox="0 0 44 44" fill="none"
           xmlns="http://www.w3.org/2000/svg">
        <circle cx="22" cy="22" r="22" transform="rotate(-180 22 22)"
                fill="#382D3F" fill-opacity="0.5" />
        <path d="M18 13L27 21.5L18 30" stroke="#715F81" stroke-width="2"
              stroke-linecap="round" stroke-linejoin="round" />
      </svg>
    </a>
  </div>
</template>

<script setup>

</script>

<style scoped lang="scss">
@import "@/assets/styles/header/headerNavigation/headerNavigation.scss";

</style>