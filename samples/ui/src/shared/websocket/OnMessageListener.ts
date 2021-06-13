import { SocketMessage } from '@/shared/websocket/SocketMessage'

export interface OnMessageListener {
  onReceive(message: SocketMessage): void
}
