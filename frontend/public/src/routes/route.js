import {createRouter, createWebHistory} from "vue-router";
import routes from "./routes";
import {handleAuthNavigation} from "../utils/routeAuth";

const router = new createRouter(
    {
        history: createWebHistory(),
        routes: routes
    }
);

router.beforeEach((to, from, next) => {
    document.title = to.meta.title;
    handleAuthNavigation(to, from, next);
});

export default router;