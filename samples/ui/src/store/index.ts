import { InjectionKey } from 'vue'
import { createStore, Store, useStore as baseUseStore } from 'vuex'

import projectsStore from '@/store/projects'

export interface State {
  [key: string]: any
}

export const key: InjectionKey<Store<State>> = Symbol()

const store = createStore({
  modules: {
    projects: projectsStore,
  },
})

export function useStore() {
  // return baseUseStore(key)
  return baseUseStore()
}

export default store
