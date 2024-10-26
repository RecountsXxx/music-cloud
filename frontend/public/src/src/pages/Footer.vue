<template>
  <footer class="footer">
    <div class="info">
      <div class="text-info">
        <router-link to="#" class="item">{{ $t('footer.About') }}</router-link>
        <router-link to="#" class="item">{{ $t('footer.Conditions') }} </router-link>
        <router-link to="#" class="item">{{ $t('footer.Blog') }}</router-link>
        <router-link to="#" class="item">{{ $t('footer.Feedback') }} </router-link>
      </div>
      <div class="image-links">
        <router-link class="item" to="#">
          <img src="@/assets/images/footer/facebook.svg" alt="facebook" />
        </router-link>
        <router-link class="item" to="#">
          <img src="@/assets/images/footer/X.svg" alt="X" />
        </router-link>
        <router-link class="item" to="#">
          <img src="@/assets/images/footer/instagram.svg" alt="instagram" />
        </router-link>
        <router-link class="item" to="#">
          <img src="@/assets/images/footer/youtube.svg" alt="youtube" />
        </router-link>
      </div>
    </div>
    <div class="underline"></div>
    <div class="bottom-info">
      <div class="f1">
        <img src="@/assets/images/footer/footer.svg" alt="" />{{ $t('footer.bottomText.text1') }}
      </div>
      <router-link to="#" class="f2">{{ $t('footer.bottomText.text2') }} </router-link>
    </div>
  </footer>
</template>

<script setup></script>

<style scoped lang="scss">
@import '@/assets/styles/Footer.scss';
</style>
