const title = 'Vibe Cloud';
const routes = [
    {
        path: '/',
        component: () => import('../pages/home/Home.vue'),
        name: "Home",
        meta: {
            title: title,
            requiresAuth: true,
        },
    },
    {
        path: '/login',
        component: () => import('../pages/auth/Login.vue'),
        name: 'Login',
        meta: {
            title: title,
        },
    },
    {
        path: '/register',
        component: () => import('../pages/auth/Registration.vue'),
        name: 'Register',
        meta: {
            title: title,
        }
    },
    {
        path: '/upload',
        component: () => import('../pages/upload/Upload.vue'),
        name: "Upload",
        meta: {
            title: title,
            requiresAuth: true,
        },
    },
]

export default routes;