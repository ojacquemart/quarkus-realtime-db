const BASE_URL = '192.168.0.15:8080'
// const BASE_URL = 'localhost:8080'

export class Urls {

  static getBackendUrl() {
    return this.baseUrl('http')
  }

  static getWebsocketUrl() {
    return this.baseUrl('ws')
  }

  static baseUrl(protocol: string) {
    return `${protocol}://${BASE_URL}`
  }

}
