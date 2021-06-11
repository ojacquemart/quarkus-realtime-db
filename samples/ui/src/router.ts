import { createRouter, createWebHistory, RouteRecordRaw, RouterOptions } from 'vue-router'

import Home from './pages/Home.vue'
import Collections from './pages/Collections.vue'
import NotFound from './pages/NotFound.vue'

const routes: RouteRecordRaw[] = [
  {path: '/', component: Home},
  {path: '/projects/:name/collections', component: Collections},
  {path: '/:path(.*)', component: NotFound},
]

const options: RouterOptions = {
  history: createWebHistory(),
  routes,
}
const router = createRouter(options)

export default router
