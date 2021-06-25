import { Module } from 'vuex'

import { StoreProjects } from '@/store/projects/StoreProjects'

import { AdminApi } from '@/apis/AdminApi'
import { ApiResponse } from '@/apis/ApiResponse'

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
    async createProject(context, text: string) {
      await AdminApi.createProject({name: text})

      await context.dispatch('fetchProjects')
    },
    async fetchProjects(context) {
      const response = await AdminApi.fetchProjects()

      context.commit('setProjects', response)
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
