const KEY_SETTINGS = 'rtdb.settings'

export class Storage {

  static setSettings(value: string) {
    this.setItem(KEY_SETTINGS, value)
  }

  static getSettings() {
    return this.getItem(KEY_SETTINGS)
  }

  private static getItem(key: string): string {
    return window.localStorage.getItem(key) || ''
  }

  private static setItem(key: string, value: string) {
    window.localStorage.setItem(key, value)
  }

}
