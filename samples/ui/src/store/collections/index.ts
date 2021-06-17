import { Module } from 'vuex'

import { StoreCollections } from '@/store/collections/StoreCollections'

import { AdminApi } from '@/apis/AdminApi'
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
    setActiveId(state: StoreCollections, index: number) {
      state.activeId = index
    },
    sendMessage(state: StoreCollections, message: SocketMessage) {
      state.websocket.sendMessage(message)
    },
  },
  actions: {
    async createCollection({state, commit}, text: string) {
      await AdminApi.createCollection({
        project: state.project?.name,
        collection: text,
      })

      await commit('appendCollection', text)
    },
    async fetchProject(context, name: string) {
      const project = await AdminApi.fetchProject(name)

      context.commit('setProject', project)
      context.commit('setCollection', project.collections?.[0])
    },
    deleteMessageId(context, id: string) {
      context.commit('sendMessage', {type: 'DELETE', content: {_id: id}})
    },
    sendMessage(context, text: string) {
      context.commit('sendMessage', {type: 'CREATE', content: JSON.parse(text)})
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
    getActiveId(state: StoreCollections): number {
      return state.activeId
    },
    hasMessages(state: StoreCollections): boolean {
      return state.messages.length > 0
    },
    getMessages(state: StoreCollections): SocketMessage[] {
      return state.messages
    },
  },
}

export default collectionsStore
