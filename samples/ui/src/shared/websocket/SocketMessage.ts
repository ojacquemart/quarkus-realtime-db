type MessageType = 'CREATE' | 'READ' | 'UPDATE' | 'DELETE'

export interface IdMessage {
  _id: string
}

export interface SocketMessage {
  type: MessageType
  content: IdMessage
}
