import { ConnectionOptions } from '@/shared/websocket/ConnectionOptions'
import { OnMessageListener } from '@/shared/websocket/OnMessageListener'
import { SocketMessage } from '@/shared/websocket/SocketMessage'

export class WebsocketClient {

  socket?: WebSocket
  listener?: OnMessageListener

  open(options: ConnectionOptions) {
    this.close()

    console.log(`websocket @ open connection "${JSON.stringify(options, null, 2)}"`)
    this.socket = new WebSocket(options.url)
    this.socket.addEventListener('open', (_: Event) => this.onOpen(options))
    this.socket.addEventListener('message', (message: MessageEvent) => this.onMessage(message))
    this.socket.addEventListener('close', (event: CloseEvent) => this.onClose(event))
  }

  close() {
    console.log('websocket @ close connection if necessary')
    this.socket?.close()
  }

  sendMessage(message: SocketMessage) {
    this.socket?.send(JSON.stringify(message))
  }

  private onOpen(options: ConnectionOptions) {
    console.log('websocket @ connection initialized, verifying the apikey...')

    this.socket?.send(JSON.stringify({type: 'HELLO', content: options.apikey}))
  }

  private onMessage(message: MessageEvent) {
    console.log('websocket @ message', message)

    try {
      const socketMessage = JSON.parse(message.data)
      this.listener?.onReceive(socketMessage)
    } catch (e) {
      console.error('websocket @ error while parsing incoming socket message', e)
    }
  }

  private onClose(event: CloseEvent) {
    console.log('websocket @ connection closed', event)
  }

}
