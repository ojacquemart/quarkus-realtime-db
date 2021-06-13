import { Module } from 'vuex'

import { StoreProjects } from '@/store/projects/StoreProjects'

import { AdminApi } from '@/apis/AdminApi'
import { ApiResponse } from '@/apis/ApiResponse'
import { NewNameRequest } from '@/apis/NewNameRequest'

const projectsStore: Module<StoreProjects, unknown> = {
  namespaced: true,
  state() {
    return {
      loading: true,
    }
  },
  mutations: {
    setProjects(state: StoreProjects, response: ApiResponse<string[]>) {
      state.loading = response.loading
      state.error = response.error
      state.items = response.data
    },
  },
  actions: {
    async createProject(context, payload: NewNameRequest) {
      await AdminApi.createProject(payload)

      await context.dispatch('fetchProjects')
    },
    async fetchProjects(context) {
      const response = await AdminApi.fetchProjects()

      context.commit('setProjects', response)
    },
  },
  getters: {},
}

export default projectsStore
