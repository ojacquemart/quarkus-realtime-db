import { Module } from 'vuex'

import { StoreCollections } from '@/store/collections/StoreCollections'

import { AdminApi } from '@/apis/AdminApi'
import { NewNameRequest } from '@/apis/NewNameRequest'
import { ProjectModel } from '@/apis/ProjectModel'
import { SocketMessage } from '@/shared/websocket/SocketMessage'

const collectionsStore: Module<StoreCollections, unknown> = {
  namespaced: true,
  state() {
    return new StoreCollections()
  },
  mutations: {
    clear(state: StoreCollections) {
      state.clear()
    },
    setProject(state: StoreCollections, project: ProjectModel) {
      state.project = project
    },
    setCollection(state: StoreCollections, name: string) {
      state.setCollection(name)
    },
    appendCollection(state: StoreCollections, name: string) {
      state.appendCollection(name)

      if (!state.hasCollection()) {
        state.setCollection(name)
      }
    },
  },
  actions: {
    async createCollection({state, commit}, request: NewNameRequest) {
      await AdminApi.createCollection({
        project: state.project?.name,
        collection: request.name,
      })

      await commit('appendCollection', request.name)
    },
    async fetchProject(context, name: string) {
      const project = await AdminApi.fetchProject(name)

      context.commit('setProject', project)
      context.commit('setCollection', project.collections?.[0])
    },
  },
  getters: {
    hasCollection(state: StoreCollections): boolean {
      return state.hasCollection()
    },
    getProjectCollectionUrlPart(state: StoreCollections): string | undefined {
      return state.getProjectCollectionUrlPart()
    },
    getApikey(state: StoreCollections): string | undefined {
      return state.project?.apikey
    },
    getCollections(state: StoreCollections): string[] {
      return state.project?.collections ?? []
    },
    getMessages(state: StoreCollections): SocketMessage[] {
      return state.messages
    },
  },
}

export default collectionsStore
