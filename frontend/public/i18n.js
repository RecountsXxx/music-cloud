import {createI18n} from 'vue-i18n';

const supportedLanguages = ['en', 'ru']; // Поддерживаемые языки
const loadedLanguages = []; // Загруженные языки

function setI18nLanguage(i18n, lang) {
    i18n.global.locale.value = lang; // Устанавливаем язык в i18n
    document.querySelector('html').setAttribute('lang', lang); // Устанавливаем язык в атрибуте 'lang' тега html
    return lang;
}

async function loadLocaleMessages(i18n, lang) {
    if (loadedLanguages.includes(lang)) {
        // Если язык уже загружен, устанавливаем его и возвращаем Promise
        return Promise.resolve(setI18nLanguage(i18n, lang));
    }

    try {
        const messages = await import(`./locales/${lang}.json`); // Загружаем сообщения для выбранного языка
        i18n.global.setLocaleMessage(lang, messages.default); // Устанавливаем загруженные сообщения в i18n
        loadedLanguages.push(lang); // Добавляем язык в список загруженных
        return setI18nLanguage(i18n, lang); // Устанавливаем язык в i18n и возвращаем его
    } catch (error) {
        console.error(`Failed to load locale messages for ${lang}:`, error); // Выводим ошибку в консоль, если не удалось загрузить сообщения
        return Promise.reject(error); // Отклоняем Promise в случае ошибки
    }
}

const i18n = createI18n({
    legacy: false,
    locale: 'ru', // Язык по умолчанию
    fallbackLocale: 'ru', // Резервный язык
    messages: {} // Пустой объект сообщений
});

export async function changeLanguage(lang) {
    if (!supportedLanguages.includes(lang)) {
        console.warn(`Unsupported language: ${lang}`); // Предупреждение, если язык не поддерживается
        return;
    }
    await loadLocaleMessages(i18n, lang); // Загружаем сообщения для выбранного языка
    localStorage.setItem('preferredLanguage', lang); // Сохраняем выбранный язык в localStorage
}

export function getPreferredLanguage() {
    const storedLang = localStorage.getItem('preferredLanguage');
    if (storedLang && supportedLanguages.includes(storedLang)) {
        return storedLang; // Возвращаем язык из localStorage, если он поддерживается
    }

    const browserLang = navigator.language.split('-')[0];
    if (supportedLanguages.includes(browserLang)) {
        return browserLang; // Возвращаем язык браузера, если он поддерживается
    }
    return 'en'; // Язык по умолчанию
}

export default i18n;