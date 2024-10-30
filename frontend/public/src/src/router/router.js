import {createRouter, createWebHistory} from "vue-router"; // Импорт необходимых модулей из Vue Router
import routes from "./routes"; // Импорт конфигурации маршрутов
import {handleAuthNavigation} from "../utils/routeAuth"; // Импорт функции для обработки авторизации маршрутов

const router = createRouter({
  history: createWebHistory(),
  routes: routes
});

// Глобальный перехватчик маршрутов
router.beforeEach((to, from, next) => {
  // Установка заголовка страницы на основе метаданных маршрута
  document.title = to.meta.title || 'Vibe Cloud';

  // Проверка доступа к маршруту на основе состояния аутентификации
  handleAuthNavigation(to, from, next);
});

export default router;