<template>
  <footer class="footer">
    <div class="info">
      <div class="text-info">
        <router-link to="#" class="item">{{ $t('footer.About') }}</router-link>
        <router-link to="#" class="item">{{ $t('footer.Conditions') }}
        </router-link>
        <router-link to="#" class="item">{{ $t('footer.Blog') }}</router-link>
        <router-link to="#" class="item">{{ $t('footer.Feedback') }}
        </router-link>
      </div>
      <div class="image-links">
        <router-link class="item" to="#">
          <img src="@/assets/images/footer/facebook.svg" alt="facebook">
        </router-link>
        <router-link class="item" to="#">
          <img src="@/assets/images/footer/X.svg" alt="X">
        </router-link>
        <router-link class="item" to="#">
          <img src="@/assets/images/footer/instagram.svg" alt="instagram">
        </router-link>
        <router-link class="item" to="#">
          <img src="@/assets/images/footer/youtube.svg" alt="youtube">
        </router-link>
      </div>
    </div>
    <div class="underline"></div>
    <div class="bottom-info">
      <div class="f1"><img src="@/assets/images/footer/footer.svg"
                           alt="">{{ $t('footer.bottomText.text1') }}
      </div>
      <router-link to="#" class="f2">{{ $t('footer.bottomText.text2') }}
      </router-link>
    </div>
  </footer>
</template>

<script setup>

</script>

<style scoped lang="scss">
$font-family: 'Inter', sans-serif;

.footer {
  font-family: $font-family;
  width: 100%;
  height: fit-content;
  display: flex;
  flex-direction: column;

  .underline {
    border-bottom: 1px solid #291F32;
    width: auto; /* Длина нижней границы */
    margin: auto 55px; /* Выравнивание по центру */
  }


  .info {
    width: 100%;
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
    padding: 50px 56px;

    .text-info {
      width: 100%;
      display: flex;

      .item {
        font-size: 16px;
        font-weight: 500;
        line-height: 19.36px;
        text-align: left;
        text-decoration: none;
        color: #BFBBBB;

        &:first-child {
          color: #F0F0F0;
        }

        margin-right: 30px;
      }
    }

    .image-links {
      display: flex;
      flex-direction: row;
      align-items: center;
      justify-content: end;
      width: 100%;
      height: fit-content;

      .item {
        border-radius: 185px;
        padding: 10px;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-self: center;
        width: 50px;
        height: 50px;
        margin-right: 10px;
        background: #291F3280;

        img {
          width: 100%;
          height: 100%;
        }
      }
    }
  }

  .bottom-info {
    margin-top: 20px;
    color: #FFFFFF;
    width: 100%;
    display: flex;

    flex-direction: column;
    align-items: center;
    justify-content: center;

    margin-bottom: 20px;

    font-family: $font-family;
    font-size: 14px;
    font-weight: 400;
    line-height: 18.2px;
    text-align: left;

    .f1 {
      display: flex;
      flex-direction: row;
      align-items: center;

      img {
        margin-right: 3px;
        width: 13px;
        height: 13px;
      }
    }

    .f2 {
      margin-top: 3px;
      color: #FFFFFF;
    }
  }
}
</style>