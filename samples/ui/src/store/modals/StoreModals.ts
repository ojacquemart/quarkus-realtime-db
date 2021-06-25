export class StoreModals {
  id?: string
  isOpened = false

  open(id: string) {
    this.id = id
    this.isOpened = true
  }

  isIdOpened(id: string) {
    return this.id === id && this.isOpened
  }

  close(id: string) {
    this.toggle({id, isOpened: false})
  }

  toggle(params: { id: string, isOpened: boolean }) {
    if (this.id === params.id) {
      this.isOpened = params.isOpened
      if (!this.isOpened) {
        this.id = undefined
      }
    }
  }

}
