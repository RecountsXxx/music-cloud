<template>
  <div class="language-dropdown">
    <select v-model="selectedLanguage" @change="changeLang">
      <option value="en">English</option>
      <option value="ua">Українська</option>
      <option value="ru">Русский</option>
    </select>
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
  margin-right: 10px;
  width: fit-content;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  position: relative;
}

select {
  text-align: center;
  max-width: fit-content;
  background-color: transparent;
  color: #1f1f1f;
  border: none;
  border-radius: 5px;
  font-size: 17px;
  font-family: Arial, Helvetica, sans-serif;
  font-weight: 600;
  appearance: none;
  -webkit-appearance: none;
  -moz-appearance: none;
  cursor: pointer;
  padding: 5px;

  option {
    font-weight: 600;
    border: none;
  }
}

select:hover {
  background-color: #d8d8d8;
}

select:focus {
  outline: none;
}
</style>
