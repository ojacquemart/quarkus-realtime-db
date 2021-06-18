export type MessageType = 'CREATE' | 'READ' | 'UPDATE' | 'DELETE'

export interface IdMessage {
  _id: string
}

export interface IdMessageWithItems extends IdMessage {
  items: IdMessage[]
}

export interface SocketMessage {
  type: MessageType
  content: IdMessage | IdMessageWithItems
}
