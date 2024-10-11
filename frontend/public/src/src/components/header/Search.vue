<template>
  <div id="search" class="search-container">
    <img src="@/assets/images/header/search.svg" class="search-icon"/>
    <input class="search" :placeholder="$t('header.search')" type="text"/>
  </div>
</template>

<script>
export default {
  name: 'Search',
  setup() {
  }
}
</script>

<style scoped lang="scss">
.search-container {
  margin-right: 173px;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: start;
  width: 570px;
  height: 48px;
  background-color: #291f32;
  border-radius: 7px;

  img {
    width: 24px;
    height: 24px;
    margin-left: 15px;
    margin-right: 10px;
  }

  .search {
    width: 100%;
    background-color: transparent;
    outline: none;
    border: none;

    font-size: 16px;
    font-weight: 400;

    &::placeholder {
      color: #685A74;
    }
  }
}
</style>
