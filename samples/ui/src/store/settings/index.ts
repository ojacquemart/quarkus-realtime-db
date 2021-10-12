import { Module } from 'vuex'

import { ApiSettings } from '@/apis/ApiSettings'

import { StoreSettings } from '@/store/settings/StoreSettings'

const settingsStore: Module<StoreSettings, unknown> = {
  namespaced: true,
  state() {
    return new StoreSettings()
  },
  mutations: {
    setSettings(state: StoreSettings, payload: ApiSettings) {
      state.setSettings(payload)
    },
  },
  actions: {
    async setSettings(context, payload: ApiSettings) {
      return context.commit('setSettings', payload)
    },
  },
  getters: {
    getApiRequest(state: StoreSettings) {
      return state.getApiRequest()
    },
    getUrl(state: StoreSettings) {
      return state.settings.url
    },
    getWebsocketUrl(state: StoreSettings) {
      return state.getWebsocketUrl()
    },
    getApikeyHeaderName(state: StoreSettings) {
      return state.settings.apikeyHeaderName
    },
    getApikeyPrefix(state: StoreSettings) {
      return state.settings.apikeyPrefix
    },
    getApikey(state: StoreSettings) {
      return state.settings.apikey
    },
  },
}

export default settingsStore
