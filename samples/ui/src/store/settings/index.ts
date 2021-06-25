import { Module } from 'vuex'

import { StoreSettings } from '@/store/settings/StoreSettings'

const settingsStore: Module<StoreSettings, unknown> = {
  namespaced: true,
  state() {
    return new StoreSettings()
  },
  mutations: {
    setUrl(state: StoreSettings, url: string) {
      state.setUrl(url)
    },
  },
  actions: {
    async setUrl(context, url: string) {
      await context.commit('setUrl', url)
    },
  },
  getters: {
    getUrl(state: StoreSettings) {
      return state.url
    },
  },
}

export default settingsStore
