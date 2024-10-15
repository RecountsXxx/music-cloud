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
  margin-right: calc(30 * 0.50vw);
  margin-left: 203px;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: start;
  max-width: 570px;
  width: 100%;
  min-width: 370px;
  height: 48px;
  background-color: #291f32;
  border-radius: 7px;

  transition: background-color 0.3s ease;

  img {
    width: 24px;
    height: 24px;
    margin-left: 15px;
    margin-right: 10px;
  }

  &:focus-within {
    background: #3E304B;
    border: 1px solid #9282FF
  }

  .search {
    width: 100%;
    background-color: transparent;
    outline: none;
    border: none;
    font-family: Inter, serif;
    font-size: 16px;
    font-weight: 400;
    line-height: 19.36px;
    color: #FFFFFF;

    &:focus {
      background: #3E304B;
    }

    &::placeholder {
      color: #685A74;
    }
  }
}
</style>
