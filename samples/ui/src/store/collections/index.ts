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
    setWebsocketUrl(state: StoreCollections, wsUrl: string) {
      state.setWebsocketUrl(wsUrl)
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
    setActiveIndex(state: StoreCollections, index: number) {
      state.activeIndex = index
    },
    sendMessage(state: StoreCollections, message: SocketMessage) {
      state.websocket.sendMessage(message)
    },
  },
  actions: {
    async createCollection({state, commit, rootGetters}, text: string) {
      await AdminApi.createCollection({
        ...rootGetters['settings/getApiRequest'],
        project: state.project?.name,
        collection: text,
      })

      return commit('appendCollection', text)
    },
    async fetchProject({commit, rootGetters}, name: string) {
      const project = await AdminApi.fetchProject({
        ...rootGetters['settings/getApiRequest'],
        name,
      })

      commit('setProject', project)
      commit('setWebsocketUrl', rootGetters['settings/getWebsocketUrl'])
      commit('setCollection', project.collections?.[0])
    },
    createEntry(context, text: string) {
      context.commit('sendMessage', {type: 'CREATE', content: JSON.parse(text)})
    },
    updateEntry(context, text: string) {
      context.commit('sendMessage', {type: 'UPDATE', content: JSON.parse(text)})
    },
    deleteEntryById(context, id: string) {
      context.commit('sendMessage', {type: 'DELETE', content: {_id: id}})
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
    getActiveMessageContent(state: StoreCollections) {
      if (state.activeIndex !== -1) {
        return JSON.stringify(state.messages[state.activeIndex]?.content)
      }

      return ''
    },
    getActiveMessageId(state: StoreCollections): string | undefined {
      return state.messages[state.activeIndex]?.content?._id
    },
    getActiveIndex(state: StoreCollections): number {
      return state.activeIndex
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
