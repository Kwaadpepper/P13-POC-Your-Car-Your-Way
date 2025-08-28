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
  private readonly subscriptions = new Map<string, StompSubscription>()

  private readonly intendedConversations = new Set<string>()

  private connectPromise?: Promise<void>
  private readonly pendingQueue: ClientCommand[] = []

  private readonly connectionChangeHandlers = new Set<(status: boolean) => void>()

  constructor(url: string) {
    this.client = new Client({
      brokerURL: url,
      reconnectDelay: 500,
      onConnect: () => {
        console.log('[STOMP] client connected.')

        this.subscribeToPendingConversations()
        this.flushPendingQueue()

        this.subscribeToPendingConversations()
        this.connectionChangeHandlers.forEach(cb => cb(true))
      },
      onDisconnect: () => {
        console.debug('[STOMP] client disconnected.')
        this.connectionChangeHandlers.forEach(cb => cb(false))
      },
      onWebSocketClose: () => {
        console.debug('[STOMP] websocket closed.')
        this.unsubscribeFromEvents()
        this.connectPromise = undefined
        this.connectionChangeHandlers.forEach(cb => cb(false))
      },
      onWebSocketError: (evt) => {
        console.error('[STOMP] websocket error', evt)
      },
      onStompError: frame => console.error('Broker reported error:', frame.headers?.['message']),
    })
  }

  onConnectionChange(cb: (status: boolean) => void): void {
    this.connectionChangeHandlers.add(cb)
    cb(this.isConnected())
  }

  async connect(): Promise<void> {
    if (
      this.connectPromise !== undefined
      && this.client.connected === false
    ) {
      return this.connectPromise
    }

    this.client.activate()

    return this.connectPromise
  }

  async disconnect(): Promise<void> {
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
    if (!destination) {
      throw new Error(`Unknown command type: ${cmd.type}`)
    }

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

  private subscribeToConversation(conversationId: string) {
    this.intendedConversations.add(conversationId)

    if (!this.client.connected) {
      console.debug('[STOMP] cannot subscribe now, not connected:', conversationId)
      return
    }

    if (this.subscriptions.has(conversationId)) {
      return
    }

    const topic = this.getTopicForConversation(conversationId)
    const userQueue = this.getUserQueueForConversation(conversationId)

    const topicSub = this.client.subscribe(topic, (message) => {
      try {
        const event = JSON.parse(message.body)
        this.handlers.forEach(handler => handler(event))
      }
      catch (e) {
        console.error('[STOMP] failed to parse incoming message', e)
      }
    })

    const userQueueSub = this.client.subscribe(userQueue, (message) => {
      try {
        const event = JSON.parse(message.body)
        this.handlers.forEach(handler => handler(event))
      }
      catch (e) {
        console.error('[STOMP] failed to parse incoming message', e)
      }
    })

    this.subscriptions.set(conversationId, topicSub)
    this.subscriptions.set(`${conversationId}-userQueue`, userQueueSub)
    console.log('[STOMP] subscribed to', topic, 'and', userQueue)
  }

  private unsubscribeFromConversation(conversationId: string) {
    try {
      this.subscriptions.get(conversationId)?.unsubscribe()
      this.subscriptions.get(`${conversationId}-userQueue`)?.unsubscribe()
    }
    catch {
      console.warn('[STOMP] failed to unsubscribe from conversation', conversationId)
    }
    this.intendedConversations.delete(conversationId)
    this.subscriptions.delete(conversationId)
    this.subscriptions.delete(`${conversationId}-userQueue`)
    console.log('[STOMP] unsubscribed from conversation', conversationId)
  }

  private subscribeToPendingConversations() {
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
      catch {
        console.warn('[STOMP] failed to unsubscribe', sub)
      }
    })
    this.subscriptions.clear()
  }

  private getTopicForConversation(conversationId: string): string {
    return `/topic/conversation/${conversationId}`
  }

  private getUserQueueForConversation(conversationId: string): string {
    return `/user/queue/conversation/${conversationId}`
  }

  private getDestination(eventType: EventType): string | null {
    switch (eventType) {
      case EventType.JOIN:
        return '/app/conversation/join'
      case EventType.LEAVE:
        return '/app/conversation/leave'
      case EventType.MESSAGE:
        return '/app/conversation/send'
      case EventType.TYPING:
        return '/app/conversation/typing'
      case EventType.HISTORY:
        return '/app/conversation/history'
      case EventType.PRESENCE:
        return '/app/conversation/presence'
      case EventType.ERROR:
        return '/topic/errors'
      default:
        return null
    }
  }
}
