import { createStore } from 'vuex'

import modals from '@/store/modals'
import projects from '@/store/projects'
import collections from '@/store/collections'
import settings from '@/store/settings'

const store = createStore({
  modules: {
    modals,
    projects,
    collections,
    settings,
  },
})

export default store
