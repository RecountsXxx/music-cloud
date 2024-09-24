<template>
  <div class="dropdown-container">
    <select v-model="selectedLanguage" @change="changeLang" class="dropdown">
      <option value="ru">Русский</option>
      <option value="ua">Українська</option>
      <option value="en">English</option>
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

<style scoped lang="scss">
/* Контейнер для DropDown */
.dropdown-container {
  margin-right: 15px;
  display: inline-block;
  background-color: transparent; /* Темный фон */
  border-radius: 5px;
}

/* Стили для select */
.dropdown {
  background-color: transparent; /* Фон DropDown */
  color: #fff; /* Белый текст */
  border: none; /* Темная рамка */
  padding: 8px;
  border-radius: 4px;
  outline: none;
  appearance: none; /* Убираем стандартную стрелку браузера */
  width: 100%;
  font-size: 1rem;
  transition: 0.3s ease;
  text-align: center;
}

/* Стили для option */
.dropdown option {
  background-color: #333; /* Фон опций */
  color: #fff; /* Цвет текста опций */
}

/* Hover и Focus стили */
.dropdown:hover {
  background-color: #4d4d4d; /* Более светлая рамка при наведении */
}
</style>
