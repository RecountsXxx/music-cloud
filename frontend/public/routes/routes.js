const title = 'Vibe Cloud';
const routes = [
    {
        path: '/',
        component: () => import('../pages/Home.vue'),
        name: "Home",
        meta: {
            title: title,
            requiresAuth: true,
        },
    },
    {
        path: '/login',
        component: () => import('../pages/Login.vue'),
        name: 'Login',
        meta: {
            title: title,
        },
    },
    {
        path: '/register',
        component: () => import('../pages/Registration.vue'),
        name: 'Register',
        meta: {
            title: title,
        }
    }
]

export default routes;