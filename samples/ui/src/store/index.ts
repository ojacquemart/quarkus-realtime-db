import { InjectionKey } from 'vue'
import { createStore, Store, useStore as baseUseStore } from 'vuex'

import projectsStore from '@/store/projects'
import collectionsStore from '@/store/collections'

export interface State {
  [key: string]: any
}

export const key: InjectionKey<Store<State>> = Symbol()

const store = createStore({
  modules: {
    projects: projectsStore,
    collections: collectionsStore,
  },
})

export function useStore() {
  // return baseUseStore(key)
  return baseUseStore()
}

export default store
