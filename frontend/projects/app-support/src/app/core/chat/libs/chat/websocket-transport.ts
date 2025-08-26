import {
  type ChatTransport,
  type ClientCommand,
  EventType,
  type ServerEvent,
} from './chat-transport'

type Handler = (evt: ServerEvent) => void

export class WebSocketTransport implements ChatTransport {
  private readonly url: string
  private readonly handlers = new Set<Handler>()
  private readonly joinedConversations = new Set<string>()
  private readonly queue: ClientCommand[] = []
  private ws?: WebSocket
  private intendedClose = false
  private reconnectDelayMs = 500 // Ms
  private readonly connectionChangeHandlers = new Set<(status: boolean) => void>()

  constructor(url: string) {
    this.url = url
  }

  async connect() {
    this.intendedClose = false
    await this.open()
  }

  private async open() {
    return new Promise<void>((resolve) => {
      const ws = new WebSocket(this.url)
      this.ws = ws

      ws.onopen = () => {
        this.onOpen()
        resolve()
      }

      ws.onmessage = this.onMessage.bind(this)
      ws.onclose = this.onClose.bind(this)
      ws.onerror = err => console.error('WebSocket error:', err)
    })
  }

  onConnectionChange(cb: (status: boolean) => void): void {
    this.connectionChangeHandlers.add(cb)
    cb(this.isConnected())
  }

  async disconnect() {
    this.intendedClose = true
    this.ws?.close()
    this.ws = undefined
  }

  isConnected(): boolean {
    return !!this.ws && this.ws.readyState === WebSocket.OPEN
  }

  onEvent(cb: (evt: ServerEvent) => void) {
    this.handlers.add(cb)
    return () => this.handlers.delete(cb)
  }

  send(cmd: ClientCommand) {
    // Mémorise les joins pour ré-adhérer après reconnexion
    if (cmd.type === 'join') {
      this.joinedConversations.add(cmd.payload.conversation)
    }
    if (cmd.type === 'leave') {
      this.joinedConversations.delete(cmd.payload.conversation)
    }

    if (this.isConnected()) {
      this.safeSend(cmd)
    }
    else {
      this.queue.push(cmd)
    }
  }

  private safeSend(cmd: ClientCommand) {
    if (!this.ws || this.ws.readyState !== WebSocket.OPEN) {
      this.queue.push(cmd)
      return
    }
    this.ws.send(JSON.stringify(cmd))
  }

  private onOpen() {
    // Ré-adhère aux conversations après reconnexion
    for (const id of this.joinedConversations) {
      this.safeSend({ type: EventType.JOIN, payload: { conversation: id } })
    }
    // Vide la file
    while (this.queue.length) {
      const cmd = this.queue.shift()!
      this.safeSend(cmd)
    }
    this.reconnectDelayMs = 500
    this.connectionChangeHandlers.forEach(cb => cb(true))
  }

  private onMessage(evt: MessageEvent) {
    const event = JSON.parse(evt.data) as ServerEvent
    this.handlers.forEach(handler => handler(event))
  }

  private onClose() {
    this.ws = undefined
    if (!this.intendedClose) {
      setTimeout(() => {
        this.reconnectDelayMs = Math.min(this.reconnectDelayMs * 2, 8000)
        this.open().catch(this.onClose.bind(this))
      }, this.reconnectDelayMs)
    }
    this.connectionChangeHandlers.forEach(cb => cb(false))
  }
}
