import { createApp } from 'vue'
import { createI18n } from 'vue-i18n'

import App from './App.vue'
import { router } from './router'

import { en } from './locales/en'

const i18n = createI18n({
  locale: 'en',
  messages: {
    en,
  },
})

createApp(App)
  .use(router)
  .use(i18n)
  .mount('#app')

import './index.css'
