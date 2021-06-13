import { ProjectModel } from '@/apis/ProjectModel'

import { Urls } from '@/shared/Urls'
import { WebsocketClient } from '@/shared/WebsocketClient'

export class StoreCollections {
  project?: ProjectModel
  collection?: string
  websocket = new WebsocketClient()

  clear() {
    this.project = undefined
    this.collection = undefined

    this.websocket.close()
  }

  hasCollection() {
    return !!this.collection
  }

  setCollection(name: string) {
    this.collection = name

    this.openWebsocket()
  }

  openWebsocket() {
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

  appendCollection(name: string) {
    if (this.project?.collections) {
      this.project.collections = this.project.collections.concat(name)
    }
  }

}
