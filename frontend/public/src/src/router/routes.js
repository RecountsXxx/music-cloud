const title = 'Vibe Cloud'
const routes = [
  {
    path: '/preview',
    component: () => import('../pages/Preview.vue'),
    name: 'Preview',
    meta: {
      title: title
    }
  },
  {
    path: '/',
    component: () => import('../pages/main/Main.vue'),
    name: '/',
    meta: {
      title: title
    }
  },
  {
    path: '/login',
    component: () => import('../pages/auth/LoginPage.vue'),
    name: 'Login',
    meta: {
      title: title
    }
  },
  {
    path: '/register',
    component: () => import('../pages/auth/RegistrationPage.vue'),
    name: 'Register',
    meta: {
      title: title
    }
  },
  {
    path: '/upload',
    component: () => import('../pages/upload/UploadPage.vue'),
    name: 'Upload',
    meta: {
      title: title,
      requiresAuth: true
    }
  },
  {
    path: '/player',
    component: () => import('@/pages/PlayerPage.vue'),
    name: 'Player',
    meta: {
      title: title,
      requiresAuth: true
    }
  }
]

export default routes
