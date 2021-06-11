import { createApp } from 'vue'
import { createI18n } from 'vue-i18n'

import App from './App.vue'
import router from './router'
import store from '@/store'

import en from './locales/en'
import './index.css'

const i18n = createI18n({
  locale: 'en',
  messages: {
    en,
  },
})

createApp(App)
  .use(router)
  .use(store)
  .use(i18n)
  .mount('#app')

