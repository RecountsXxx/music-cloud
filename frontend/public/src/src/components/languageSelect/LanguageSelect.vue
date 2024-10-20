<template>
  <div class="dropdown-container">
    <div class="dropdown" @click="toggleDropdown">{{ currentLanguage }}</div>
    <ul v-if="isDropdownOpen" class="dropdown-list">
      <li v-for="(lang, code) in languages" :key="code"
          @click="changeLang(code)">{{ lang }}
      </li>
    </ul>
  </div>
</template>

<script>
import {onBeforeUnmount, onMounted, ref} from 'vue'
import {changeLanguage, getPreferredLanguage} from '../../../i18n.js'

export default {
  name: 'LanguageDropdown',
  setup() {
    const languages = {
      en: 'Eng',
      ru: 'Рус',
      ua: 'Укр'
    }

    const isDropdownOpen = ref(false)
    const currentLanguage = ref(languages[getPreferredLanguage()])

    function changeLang(code) {
      changeLanguage(code)
      currentLanguage.value = languages[code]
      isDropdownOpen.value = false
    }

    function toggleDropdown() {
      isDropdownOpen.value = !isDropdownOpen.value
    }

    function handleClickOutside(event) {
      // Проверяем, был ли клик вне dropdown контейнера
      if (!event.target.closest('.dropdown-container')) {
        isDropdownOpen.value = false
      }
    }

    // Добавляем глобальный слушатель событий на клик
    onMounted(() => {
      document.addEventListener('click', handleClickOutside)
    })

    // Убираем слушатель событий, когда компонент уничтожается
    onBeforeUnmount(() => {
      document.removeEventListener('click', handleClickOutside)
    })

    return {
      languages,
      isDropdownOpen,
      currentLanguage,
      changeLang,
      toggleDropdown
    }
  }
}
</script>

<style scoped lang="scss">
@import '@/assets/styles/topHeader/langDropDown/langDropDown';
</style>
