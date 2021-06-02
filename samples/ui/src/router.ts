import { createRouter, createWebHistory, RouteRecordRaw, RouterOptions } from 'vue-router'

import Home from './pages/Home.vue'
import NotFound from './pages/NotFound.vue'

const routes: RouteRecordRaw[] = [
    {path: '/', component: Home, meta: {title: 'Home'}},
    {path: '/:path(.*)', component: NotFound},
]

const options: RouterOptions = {
    history: createWebHistory(),
    routes,
}
export const router = createRouter(options)
