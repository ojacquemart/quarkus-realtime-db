import { ProjectModel } from '@/apis/ProjectModel'

import { Urls } from '@/shared/Urls'
import { OnMessageListener } from '@/shared/websocket/OnMessageListener'
import { SocketMessage } from '@/shared/websocket/SocketMessage'
import { WebsocketClient } from '@/shared/websocket/WebsocketClient'

export class StoreCollections implements OnMessageListener {
  project?: ProjectModel
  collection?: string

  websocket = new WebsocketClient()
  messages: SocketMessage[] = []

  constructor() {
    this.websocket.listener = this
  }

  clear() {
    this.project = undefined
    this.collection = undefined

    this.websocket.close()
    this.messages = []
  }

  hasCollection() {
    return !!this.collection
  }

  setCollection(name: string) {
    this.collection = name

    this.openWebsocket()
  }

  appendCollection(name: string) {
    if (this.project?.collections) {
      this.project.collections = this.project.collections.concat(name)
    }
  }

  openWebsocket() {
    this.messages = []
    if (!this.hasCollection()) {

      return
    }

    const url = [Urls.getWebsocketUrl(), this.getProjectCollectionUrlPart()].join('/')
    const apikey = this.project?.apikey
    if (!apikey) {
      throw Error('No apikey defined!')
    }

    this.websocket.open({url, apikey})
  }

  getProjectCollectionUrlPart(): string | undefined {
    if (!this.collection) {
      return undefined
    }

    return `${this.project?.name}/${this.collection}/_all`
  }

  onReceive(message: SocketMessage) {
    switch (message.type) {
      case 'DELETE':
        this.messages = this.messages.filter((it: SocketMessage) => {
          return it.content._id !== message.content._id
        })
        break
      case 'CREATE':
        this.messages.push(message)
        break
    }
  }

}
