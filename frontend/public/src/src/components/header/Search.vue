<template>
  <div class="header__search__field">
    <svg :class="{ active: isActive }" class="search__icon" width="24"
         height="24" viewBox="0 0 24 24"
         fill="none"
         xmlns="http://www.w3.org/2000/svg">
      <path
        d="M10.5 19C15.1944 19 19 15.1944 19 10.5C19 5.8056 15.1944 2 10.5 2C5.8056 2 2 5.8056 2 10.5C2 15.1944 5.8056 19 10.5 19Z"
        stroke="white" stroke-width="2" stroke-linejoin="round" />
      <path
        d="M13.3287 7.17155C12.6049 6.4477 11.6049 6 10.5003 6C9.39572 6 8.39573 6.4477 7.67188 7.17155"
        stroke="white" stroke-width="2" stroke-linecap="round"
        stroke-linejoin="round" />
      <path d="M16.6094 16.6113L20.852 20.854" stroke="white" stroke-width="2"
            stroke-linecap="round" stroke-linejoin="round" />
    </svg>

    <input v-model="searchText" class="search__input"
           @focus="onFocus"
           @blur="onBlur"
           :placeholder="$t('header.search')"
           type="text" />


    <span class="clearSearch" v-if="searchText" @click="clearSearch">
        <clear-search />
      </span>
  </div>
</template>

<script>
import ClearSearch from '@/components/clearSearch/ClearSearch.vue';
import { ref } from 'vue';

export default {
  name: 'Search',
  components: { ClearSearch },
  setup() {
    const searchText = ref('');
    const isActive = ref(false);

    function clearSearch() {
      this.searchText = '';
      this.isActive = false;
    }

    function onFocus() {
      this.isActive = true;
    }

    function onBlur() {
      if (!this.searchText) {
        this.isActive = false;
      }
    }

    return {
      isActive,
      onFocus,
      onBlur,
      clearSearch,
      searchText,
    };
  },
};
</script>

<style scoped lang="scss">
@import "@/assets/styles/Header/search/search.scss";
</style>
