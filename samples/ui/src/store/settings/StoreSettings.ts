import { Urls } from '@/shared/Urls'

export class StoreSettings {
  url?: string

  constructor() {
    this.url = Urls.getBackendUrl()
  }

  setUrl(url: string) {
    Urls.setUrl(url)

    this.url = url
  }

}
