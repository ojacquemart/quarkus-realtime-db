import { ApiRequest, HttpHeaders } from '@/apis/ApiRequest'
import { ApiSettings } from '@/apis/ApiSettings'

import { Storage } from '@/shared/Storage'

const DEFAULT_URL = 'http://localhost:8080'

export class StoreSettings {

  settings: ApiSettings

  constructor() {
    this.settings = this.getSettings()
  }

  setSettings(settings: ApiSettings) {
    const settingsAsString = JSON.stringify(settings)

    Storage.setSettings(settingsAsString)
  }

  getWebsocketUrl(): string {
    const {url} = this.settings

    return url.replace(/^http/, 'ws')
  }

  getApiRequest(): ApiRequest {
    return {url: this.settings.url, headers: this.getHeaders()}
  }

  getHeaders(): HttpHeaders {
    if (!this.isApikeyDefined()) {
      return {}
    }

    const {apikeyHeaderName, apikeyPrefix, apikey} = this.settings

    return {
      [apikeyHeaderName]: `${apikeyPrefix} ${apikey}`.trim(),
    }
  }

  isApikeyDefined() {
    return this.settings?.apikey?.trim().length > 0
  }

  private getSettings() {
    const storageSettings = Storage.getSettings()
    if (storageSettings.length === 0) {
      return this.defaultSettings()
    }

    try {
      return JSON.parse(storageSettings) as ApiSettings
    } catch (e) {
      console.warn('Unable to store settings', e)

      return this.defaultSettings()
    }
  }

  private defaultSettings(): ApiSettings {
    return {
      url: DEFAULT_URL,
      apikeyHeaderName: '',
      apikeyPrefix: '',
      apikey: '',
    }
  }

}
