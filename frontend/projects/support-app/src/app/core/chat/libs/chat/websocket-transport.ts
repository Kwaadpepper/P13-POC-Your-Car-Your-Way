// Transport WebSocket minimal + auto-reconnexion avec ré-abonnement aux conversations

import type { ChatTransport, ClientCommand, ServerEvent } from './chat-transport'

type Handler = (evt: ServerEvent) => void

export class WebSocketTransport implements ChatTransport {
  private readonly url: string
  private token?: string
  private ws?: WebSocket
  private readonly handlers = new Set<Handler>()
  private intendedClose = false
  private reconnectDelayMs = 500
  private readonly joinedConversations = new Set<string>()
  private readonly queue: ClientCommand[] = []

  constructor(url: string) {
    this.url = url
  }

  async connect(token?: string): Promise<void> {
    this.token = token
    this.intendedClose = false
    await this.open()
  }

  private open(): Promise<void> {
    return new Promise((resolve) => {
      const qs = this.token ? `?token=${encodeURIComponent(this.token)}` : ''
      const ws = new WebSocket(`${this.url}${qs}`)
      this.ws = ws

      ws.onopen = () => {
        // Ré-adhère aux conversations après reconnexion
        for (const id of this.joinedConversations) {
          this.safeSend({ type: 'join', payload: { conversation: id } })
        }
        // Vide la file
        while (this.queue.length) {
          const cmd = this.queue.shift()!
          this.safeSend(cmd)
        }
        this.reconnectDelayMs = 500
        resolve()
      }

      ws.onmessage = (ev) => {
        try {
          const evt = JSON.parse(ev.data) as ServerEvent
          this.handlers.forEach(h => h(evt))
        }
        catch {
          // ignore
        }
      }

      ws.onerror = () => {
        // Laisse onclose gérer la reconnexion
      }

      ws.onclose = () => {
        this.ws = undefined
        if (!this.intendedClose) {
          setTimeout(() => {
            this.reconnectDelayMs = Math.min(this.reconnectDelayMs * 2, 8000)
            this.open().catch(() => {
              // Retentes
            })
          }, this.reconnectDelayMs)
        }
      }
    })
  }

  async disconnect(): Promise<void> {
    this.intendedClose = true
    this.ws?.close()
    this.ws = undefined
  }

  isConnected(): boolean {
    return !!this.ws && this.ws.readyState === WebSocket.OPEN
  }

  onEvent(cb: (evt: ServerEvent) => void): () => void {
    this.handlers.add(cb)
    return () => this.handlers.delete(cb)
  }

  send(cmd: ClientCommand): void {
    // Mémorise les joins pour ré-adhérer après reconnexion
    if (cmd.type === 'join') this.joinedConversations.add(cmd.payload.conversation)
    if (cmd.type === 'leave') this.joinedConversations.delete(cmd.payload.conversation)

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
}
