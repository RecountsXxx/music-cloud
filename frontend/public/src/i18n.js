import { createI18n } from 'vue-i18n'

const supportedLanguages = ['en', 'ru', 'ua'] // Поддерживаемые языки
const loadedLanguages = [] // Список уже загруженных языков

/**
 * Устанавливает язык в i18n и атрибут 'lang' в html
 * @param {Object} i18n - экземпляр i18n
 * @param {string} lang - язык, который нужно установить
 * @returns {string} Установленный язык
 */
function setI18nLanguage(i18n, lang) {
  i18n.global.locale.value = lang // Устанавливаем текущий язык в i18n
  document.querySelector('html').setAttribute('lang', lang) // Устанавливаем язык в атрибут 'lang' тега html
  return lang
}

/**
 * Загружает локализационные сообщения для указанного языка и устанавливает язык в i18n
 * @param {Object} i18n - экземпляр i18n
 * @param {string} lang - язык, который нужно загрузить и установить
 * @returns {Promise} Возвращает Promise с установленным языком или ошибкой
 */
async function loadLocaleMessages(i18n, lang) {
  if (loadedLanguages.includes(lang)) {
    // Если язык уже загружен, сразу устанавливаем его
    return Promise.resolve(setI18nLanguage(i18n, lang))
  }

  try {
    const messages = await import(`./src/locales/${lang}.json`) // Динамическая загрузка файла с сообщениями для языка
    i18n.global.setLocaleMessage(lang, messages.default) // Добавляем загруженные сообщения в i18n
    loadedLanguages.push(lang) // Добавляем язык в список загруженных
    return setI18nLanguage(i18n, lang) // Устанавливаем язык и возвращаем его
  } catch (error) {
    console.error(`Failed to load locale messages for ${lang}:`, error) // Обрабатываем ошибку загрузки
    return Promise.reject(error) // Возвращаем ошибку
  }
}

// Создание экземпляра i18n с начальной конфигурацией
const i18n = createI18n({
  legacy: false, // Используем Composition API
  locale: 'ru', // Язык по умолчанию
  fallbackLocale: 'ru', // Резервный язык на случай отсутствия локализации
  messages: {} // Пустые сообщения, так как будем их загружать динамически
})

/**
 * Изменяет язык приложения, загружая локализационные данные и сохраняя язык в localStorage
 * @param {string} lang - язык, который нужно установить
 * @returns {Promise<void>}
 */
export async function changeLanguage(lang) {
  if (!supportedLanguages.includes(lang)) {
    console.warn(`Unsupported language: ${lang}`) // Предупреждаем, если язык не поддерживается
    return
  }
  await loadLocaleMessages(i18n, lang) // Загружаем и устанавливаем язык
  localStorage.setItem('preferredLanguage', lang) // Сохраняем предпочтительный язык в localStorage
}

/**
 * Получает предпочтительный язык из localStorage или браузера
 * @returns {string} Предпочтительный язык
 */
export function getPreferredLanguage() {
  const storedLang = localStorage.getItem('preferredLanguage') // Получаем язык из localStorage
  if (storedLang && supportedLanguages.includes(storedLang)) {
    return storedLang // Возвращаем язык из localStorage, если он поддерживается
  }

  const browserLang = navigator.language.split('-')[0] // Получаем язык браузера
  if (supportedLanguages.includes(browserLang)) {
    return browserLang // Возвращаем язык браузера, если он поддерживается
  }
  return 'ru' // Язык по умолчанию, если ничего не найдено
}

export default i18n
