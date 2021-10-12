import { Module } from 'vuex'

import { AdminApi } from '@/apis/AdminApi'
import { ApiResponse } from '@/apis/ApiResponse'

import { StoreProjects } from '@/store/projects/StoreProjects'

const projectsStore: Module<StoreProjects, unknown> = {
  namespaced: true,
  state() {
    return new StoreProjects()
  },
  mutations: {
    setProjects(state: StoreProjects, response: ApiResponse<string[]>) {
      state.copy(response)
    },
  },
  actions: {
    async createProject({dispatch, rootGetters}, text: string) {
      const newProject = {...rootGetters['settings/getApiRequest'], name: text}
      await AdminApi.createProject(newProject)

      await dispatch('fetchProjects')
    },
    async fetchProjects({commit, rootGetters}) {
      const response = await AdminApi.fetchProjects(rootGetters['settings/getApiRequest'])

      commit('setProjects', response)
    },
  },
  getters: {
    isPending(state: StoreProjects) {
      return state.isPending()
    },
    isError(state: StoreProjects) {
      return state.isError()
    },
  },
}

export default projectsStore
