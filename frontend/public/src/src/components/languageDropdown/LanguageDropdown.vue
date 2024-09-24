<template>
  <div class="language-dropdown">
    <select v-model="selectedLanguage" @change="changeLang">
      <option value="en">English</option>
      <option value="ua">Українська</option>
      <option value="ru">Русский</option>
    </select>
    <span class="custom-arrow"></span>
  </div>
</template>

<script>
import { changeLanguage, getPreferredLanguage } from '../../../i18n.js'
import { ref, watch } from 'vue'

export default {
  name: 'LanguageDropdown',
  setup() {
    const selectedLanguage = ref(getPreferredLanguage())

    // Синхронизируем изменение языка в userStore
    watch(selectedLanguage, (newLang) => {
      changeLanguage(newLang)
    })

    const changeLang = () => {
      changeLanguage(selectedLanguage.value)
    }

    return {
      selectedLanguage,
      changeLang
    }
  }
}
</script>

<style scoped>
/* Добавление стилей для выпадающего меню */
.language-dropdown {
  position: relative;
  display: inline-block;
}

select {
  background-color: #1f1f1f;
  color: #fff;
  border: 1px solid #444;
  border-radius: 5px;
  padding: 10px;
  font-size: 16px;
  appearance: none;
  -webkit-appearance: none;
  -moz-appearance: none;
  cursor: pointer;
  padding-right: 40px;
}

select:hover {
  background-color: #333;
}

select:focus {
  outline: none;
  border-color: #777;
}

.custom-arrow {
  content: '';
  position: absolute;
  right: 10px;
  top: 50%;
  transform: translateY(-50%);
  width: 0;
  height: 0;
  border-left: 6px solid transparent;
  border-right: 6px solid transparent;
  border-top: 6px solid #fff;
  pointer-events: none;
}
</style>
