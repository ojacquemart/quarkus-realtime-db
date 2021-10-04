import { Module } from 'vuex'

import { StoreModals } from '@/store/modals/StoreModals'

const modalsStore: Module<StoreModals, unknown> = {
  namespaced: true,
  state() {
    return new StoreModals()
  },
  mutations: {
    open(state: StoreModals, id: string) {
      state.open(id)
    },
    close(state: StoreModals, id: string) {
      state.close(id)
    },
    toggle(state: StoreModals, params: { id: string, isOpened: boolean }) {
      state.toggle(params)
    },
  },
  actions: {},
  getters: {
    id: (state: StoreModals) => {
      return state.id
    },
    hasSelection: (state: StoreModals) => {
      return !!state.id
    },
    isOpened: (state: StoreModals) => (id: string) => {
      return state.isIdOpened(id)
    },
  },
}

export default modalsStore
