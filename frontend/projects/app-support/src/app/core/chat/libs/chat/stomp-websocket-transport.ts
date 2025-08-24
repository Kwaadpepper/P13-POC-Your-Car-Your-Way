import { Client, StompSubscription } from '@stomp/stompjs'

import {
  type ChatTransport,
  type ClientCommand,
  EventType,
  type ServerEvent,
} from './chat-transport'

type Handler = (evt: ServerEvent) => void

export class StompWebSocketTransport implements ChatTransport {
  private readonly client: Client
  private readonly handlers = new Set<Handler>()
  private readonly subscriptions = new Map<string, StompSubscription>() // conversationId -> subscription
  private intendedDisconnect = false

  private readonly intendedConversations = new Set<string>()

  private connectPromise?: Promise<void>
  private readonly pendingQueue: ClientCommand[] = []

  constructor(url: string) {
    this.client = new Client({
      brokerURL: url,
      reconnectDelay: 500,
      onConnect: () => this.onConnect(),
      onStompError: frame => console.error('Broker reported error:', frame.headers?.['message']),
    })
  }

  async connect(): Promise<void> {
    this.intendedDisconnect = false

    // Si une connexion est en cours, on réutilise la promise
    if (
      this.connectPromise !== undefined
      && this.client.connected === false
    ) {
      return this.connectPromise
    }

    this.client.activate()

    this.connectPromise = new Promise<void>((resolve) => {
      this.client.onWebSocketClose = () => {
        console.log('[STOMP] websocket closed')
        this.unsubscribeFromEvents()
        this.connectPromise = undefined
        if (!this.intendedDisconnect) {
          // stompjs gèrera la reconnexion selon reconnectDelay
        }
      }

      const originalOnConnect = this.client.onConnect
      this.client.onConnect = (frame) => {
        try {
          if (originalOnConnect) {
            originalOnConnect(frame)
          }
        }
        catch (e) {
          console.warn('[STOMP] original onConnect error', e)
        }

        this.subscribeToPendingConversations()
        this.flushPendingQueue()
        resolve()
      }
    })

    return this.connectPromise
  }

  async disconnect(): Promise<void> {
    this.intendedDisconnect = true
    try {
      await this.client.deactivate()
    }
    catch (e) {
      console.warn('[STOMP] deactivate failed', e)
    }

    this.unsubscribeFromEvents()
    this.connectPromise = undefined
  }

  isConnected(): boolean {
    return this.client.connected
  }

  onEvent(cb: (evt: ServerEvent) => void): () => void {
    this.handlers.add(cb)
    if (this.isConnected()) {
      this.subscribeToPendingConversations()
    }
    return () => {
      this.handlers.delete(cb)
      if (this.handlers.size === 0) {
        this.unsubscribeFromEvents()
      }
    }
  }

  send(cmd: ClientCommand): void {
    const destination = this.getDestination(cmd.type)
    if (!destination) return

    if (cmd.type === EventType.JOIN) {
      const conversationId = cmd.payload.conversation
      this.subscribeToConversation(conversationId)
    }
    if (cmd.type === EventType.LEAVE) {
      const conversationId = cmd.payload.conversation
      this.unsubscribeFromConversation(conversationId)
    }

    if (this.client?.connected) {
      this.safePublish(destination, cmd)
    }
    else {
      this.pendingQueue.push(cmd)
    }
  }

  private safePublish(destination: string, cmd: ClientCommand) {
    try {
      this.client.publish({
        destination,
        body: JSON.stringify(cmd.payload),
      })
    }
    catch (e) {
      console.warn('[STOMP] publish failed, queueing', e)
      this.pendingQueue.push(cmd)
    }
  }

  private flushPendingQueue() {
    if (!this.client.connected) {
      return
    }
    while (this.pendingQueue.length) {
      const cmd = this.pendingQueue.shift()!
      const destination = this.getDestination(cmd.type)
      if (destination) {
        this.safePublish(destination, cmd)
      }
    }
  }

  private onConnect() {
    console.log('STOMP client connected.')
    // (Re)créer les subscriptions pour les conversations voulues
    this.subscribeToPendingConversations()
    // flushPendingQueue est appelé depuis la Promise connect() pour garantir l'ordre
  }

  private subscribeToConversation(conversationId: string) {
    this.intendedConversations.add(conversationId)

    if (!this.client.connected) {
      // on ne doit pas appeler client.subscribe si pas connecté
      console.debug('[STOMP] cannot subscribe now, not connected:', conversationId)
      return
    }

    if (this.subscriptions.has(conversationId)) {
      return
    }

    const topic = this.getTopicForConversation(conversationId)
    const sub = this.client.subscribe(topic, (message) => {
      try {
        const event = JSON.parse(message.body)
        this.handlers.forEach(handler => handler(event))
      }
      catch (e) {
        console.error('[STOMP] failed to parse incoming message', e)
      }
    })
    this.subscriptions.set(conversationId, sub)
    console.log('[STOMP] subscribed to', topic)
  }

  private unsubscribeFromConversation(conversationId: string) {
    try {
      this.subscriptions.get(conversationId)?.unsubscribe()
    }
    catch { /* ignore */ }
    this.intendedConversations.delete(conversationId)
    this.subscriptions.delete(conversationId)
    console.log('[STOMP] unsubscribed from conversation', conversationId)
  }

  private subscribeToPendingConversations() {
    // pour chaque conversation qu'on souhaite, (re)crée l'abonnement si nécessaire
    this.intendedConversations.forEach((conversationId) => {
      if (!this.subscriptions.has(conversationId)) {
        this.subscribeToConversation(conversationId)
      }
    })
  }

  private unsubscribeFromEvents() {
    this.subscriptions.forEach((sub) => {
      try {
        sub.unsubscribe()
      }
      catch { /* ignore */ }
    })
    this.subscriptions.clear()
  }

  private getTopicForConversation(conversationId: string): string {
    return `/topic/conversation/${conversationId}`
  }

  private getDestination(eventType: EventType): string | null {
    switch (eventType) {
      case EventType.JOIN:
        return '/app/conversation/join'
      case EventType.LEAVE:
        return '/app/conversation/leave'
      case EventType.MESSAGE:
        return '/app/chat/send'
      case EventType.TYPING:
        return '/app/chat/typing'
      case EventType.HISTORY:
        return '/app/chat/history'
      case EventType.PRESENCE:
        return '/app/chat/presence'
      case EventType.ERROR:
        return '/topic/errors'
      default:
        return null
    }
  }
}
