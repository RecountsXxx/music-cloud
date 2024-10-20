<template>
  <div id="search__wrapper">
    <a href="#" class="search__button search__button--prev">
      <svg width="44" height="44" viewBox="0 0 44 44" fill="none"
           xmlns="http://www.w3.org/2000/svg">
        <circle cx="22" cy="22" r="22" fill="#382D3F" fill-opacity="0.5" />
        <path d="M26 31L17 22.5L26 14" stroke="#715F81" stroke-width="2"
              stroke-linecap="round" stroke-linejoin="round" />
      </svg>
    </a>
    <a href="#" class="search__button search__button--next">
      <svg width="44" height="44" viewBox="0 0 44 44" fill="none"
           xmlns="http://www.w3.org/2000/svg">
        <circle cx="22" cy="22" r="22" transform="rotate(-180 22 22)"
                fill="#382D3F" fill-opacity="0.5" />
        <path d="M18 13L27 21.5L18 30" stroke="#715F81" stroke-width="2"
              stroke-linecap="round" stroke-linejoin="round" />
      </svg>
    </a>
    <div class="search__input__field">
      <img src="@/assets/images/header/search.svg" class="search-icon" />
      <input v-model="searchText" class="search__input"
             :placeholder="$t('header.search')"
             type="text" />
      <span v-if="searchText" @click="clearSearch">
        <clear-search />
      </span>
    </div>
  </div>
</template>

<script>
import ClearSearch from '@/components/clearSearch/ClearSearch.vue'
import { ref } from 'vue'

export default {
  name: 'Search',
  components: { ClearSearch },
  setup() {
    const searchText = ref('')

    function clearSearch() {
      this.searchText = ''
    }

    return {
      clearSearch,
      searchText
    }
  }
}
</script>

<style scoped lang="scss">
@import "@/assets/styles/topHeader/search/search.scss";
</style>
