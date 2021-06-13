export interface ConnectionOptions {
  url: string
  apikey: string
}

export class WebsocketClient {

  socket?: WebSocket

  open(options: ConnectionOptions) {
    this.close()

    console.log(`websocket @ open connection "${JSON.stringify(options, null, 2)}"`)
    this.socket = new WebSocket(options.url)
    this.socket.addEventListener('open', (_: Event) => this.onOpen( options))
    this.socket.addEventListener('message', this.onMessage)
    this.socket.addEventListener('close', this.onClose)
  }

  close() {
    console.log('websocket @ close connection if necessary')
    this.socket?.close()
  }

  private onOpen(options: ConnectionOptions) {
    console.log('websocket @ connection initialized, verifying the apikey...')

    this.socket?.send(JSON.stringify({type: 'HELLO', content: options.apikey}))
  }

  private onMessage(message: MessageEvent) {
    console.log('websocket @message', message)
  }

  private onClose(event: Event) {
    console.log('websocket @ connection closed', event)
  }

}
