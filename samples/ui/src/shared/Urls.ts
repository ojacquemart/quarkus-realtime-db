const BASE_URL = 'http://localhost:8080'

const HTTP_SCHEME = 'http'
const KEY_URL = 'rtdb.url'

export class Urls {

  static getBackendUrl() {
    return this.baseUrl('http')
  }

  static getWebsocketUrl() {
    return this.baseUrl('ws')
  }

  static baseUrl(protocol: string) {
    const url = (window.localStorage.getItem(KEY_URL) || BASE_URL).replace(HTTP_SCHEME, '')

    return `${protocol}${url}`
  }

  static setUrl(url) {
    window.localStorage.setItem(KEY_URL, url)
  }

}
