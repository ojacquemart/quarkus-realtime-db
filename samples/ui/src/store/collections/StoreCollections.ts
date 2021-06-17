import { ProjectModel } from '@/apis/ProjectModel'

import { Urls } from '@/shared/Urls'
import { OnMessageListener } from '@/shared/websocket/OnMessageListener'
import { IdMessageWithItems, SocketMessage } from '@/shared/websocket/SocketMessage'
import { WebsocketClient } from '@/shared/websocket/WebsocketClient'

export class StoreCollections implements OnMessageListener {
  project?: ProjectModel
  collection?: string
  activeId: number = -1

  websocket = new WebsocketClient()
  messages: SocketMessage[] = []

  constructor() {
    this.websocket.listener = this
  }

  clear() {
    this.project = undefined
    this.collection = undefined
    this.activeId = -1

    this.websocket.close()
    this.messages = []
  }

  hasCollection() {
    return !!this.collection
  }

  setCollection(name: string) {
    this.collection = name
    this.activeId = -1

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
      case 'CREATE':
        this.createMessage(message)
        break
      case 'READ':
        this.readMessage(message)
        break
      case 'UPDATE':
        this.updateMessage(message)
        break
      case 'DELETE':
        this.removeMessage(message)
        break
    }
  }

  private createMessage(message: SocketMessage) {
    this.messages.push(message)
  }

  private readMessage(message: SocketMessage) {
    const messageWithItems = (message.content as IdMessageWithItems)
    this.messages.push(...messageWithItems.items.map(it => {
      return {
        type: message.type,
        content: it,
      }
    }))
  }

  private updateMessage(message: SocketMessage) {
    this.switchType(message)
  }

  private removeMessage(message: SocketMessage) {
    this.switchType(message)

    // animate the new message type and then delete the message
    // css animation is 1 second
    setTimeout(() => {
      this.messages = this.messages.filter((it: SocketMessage) => {
        return it.content._id !== message.content._id
      })
    }, 1_000)
  }

  private switchType(message: SocketMessage) {
    const actual = this.messages.find((it: SocketMessage) => {
      return it.content._id === message.content._id
    })
    if (actual) {
      actual.type = message.type
      actual.content = message.content
    }
  }
}
