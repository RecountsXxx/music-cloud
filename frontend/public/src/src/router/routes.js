const title = 'Vibe Cloud';
const routes = [
    {
        path: '/',
        component: () => import('../pages/home/HomePage.vue'),
        name: "Home",
        meta: {
            title: title,
            requiresAuth: true,
        },
    },
    {
        path: '/login',
        component: () => import('../pages/auth/LoginPage.vue'),
        name: 'Login',
        meta: {
            title: title,
        },
    },
    {
        path: '/register',
        component: () => import('../pages/auth/RegistrationPage.vue'),
        name: 'Register',
        meta: {
            title: title,
        }
    },
    {
        path: '/upload',
        component: () => import('../pages/upload/UploadPage.vue'),
        name: "Upload",
        meta: {
            title: title,
            requiresAuth: true,
        },
    },
    {
        path: '/player',
        component: () => import('@/pages/PlayerPage.vue'),
        name: "Player",
        meta: {
            title: title,
            requiresAuth: true,
        },
    },
]

export default routes;