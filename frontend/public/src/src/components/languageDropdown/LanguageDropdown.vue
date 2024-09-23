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
import { useUserStore } from '@/stores/userStore.js'

export default {
  name: 'LanguageDropdown',
  setup() {
    const userStore = useUserStore()
    const selectedLanguage = userStore.language ? userStore.language : getPreferredLanguage()

    function changeLang() {
      const language = this.selectedLanguage
      console.log(language)
      changeLanguage(language)
    }

    return {
      selectedLanguage,
      changeLang
    }
  }
}
</script>

<style scoped lang="scss">
.language-dropdown {
  max-width: 120px;
  position: relative;
  display: inline-block;
}

select {
  //max-width: 100%;
  //color: black;
  //background-color: transparent;
  //width: 150px;
  //border: none;
  //border-radius: 5px;
  //padding: 10px;
  //font-size: 16px;
  //appearance: none; /* Убираем стандартные стрелки браузера */
  //-webkit-appearance: none;
  //-moz-appearance: none;
  //cursor: pointer;
  //transition: all 0.3s ease;

  background-color: #1f1f1f;
  color: black;
  border: 1px solid #444;
  border-radius: 5px;
  padding: 10px;
  font-size: 16px;
  appearance: none; /* Убираем стандартные стрелки браузера */
  -webkit-appearance: none;
  -moz-appearance: none;
  cursor: pointer;
  transition: all 0.3s ease;
  padding-right: 40px; /* Дополнительное пространство для стрелки */

  option {
    background-color: #f2f2f2;
  }
}

select:hover {
  background-color: #c8c8c8;
}

select:focus {
  outline: none;
  border-color: #777;
}

.language-dropdown:hover::after {
  content: '▼';
  position: absolute;
  right: 10px;
  top: 50%;
  transform: translateY(-50%);
  pointer-events: none;
  color: black;
}

.language-dropdown select {
  padding-right: 30px;
}
</style>
