import { createStore } from 'vuex'

import projects from '@/store/projects'
import collections from '@/store/collections'

const store = createStore({
  modules: {
    projects,
    collections,
  },
})

export default store
