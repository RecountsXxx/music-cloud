<template>
  <router-link :to="{ name:'Upload' }" class="Upload">
    <svg width="44" height="44" viewBox="0 0 44 44" fill="none"
         xmlns="http://www.w3.org/2000/svg">
      <circle cx="22" cy="22" r="22" fill="#382D3F" fill-opacity="0.5" />
      <path
        d="M15.8389 20.1348C13.6374 20.6583 12 22.6376 12 24.9993C12 27.7607 14.2386 29.9993 17 29.9993C17.4737 29.9993 17.932 29.9334 18.3663 29.8103"
        stroke="white" stroke-width="1.5" stroke-linecap="round"
        stroke-linejoin="round" />
      <path
        d="M28.0274 20.1348C30.2289 20.6583 31.8662 22.6376 31.8662 24.9993C31.8662 27.7607 29.6277 29.9993 26.8662 29.9993C26.3925 29.9993 25.9342 29.9334 25.5 29.8103"
        stroke="white" stroke-width="1.5" stroke-linecap="round"
        stroke-linejoin="round" />
      <path d="M28 20C28 16.6863 25.3137 14 22 14C18.6863 14 16 16.6863 16 20"
            stroke="white" stroke-width="1.5" stroke-linecap="round"
            stroke-linejoin="round" />
      <path d="M18.5312 23.9396L21.9985 20.4609L25.5645 23.9991" stroke="white"
            stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
      <path d="M22 28.9996V22.2305" stroke="white" stroke-width="1.5"
            stroke-linecap="round" stroke-linejoin="round" />
    </svg>
  </router-link>
</template>

<script setup>

</script>

<style scoped lang="scss">
.Upload {
  min-width: 44px;
  min-height: 44px;

  svg {
    width: inherit;
    height: inherit;
  }
}
</style>