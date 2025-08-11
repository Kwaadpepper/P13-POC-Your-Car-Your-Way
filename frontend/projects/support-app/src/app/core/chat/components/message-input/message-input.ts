import { CommonModule } from '@angular/common'
import { Component, EventEmitter, OnDestroy, Output } from '@angular/core'
import { FormsModule } from '@angular/forms'

import { ButtonModule } from 'primeng/button'
import { TextareaModule } from 'primeng/textarea'

@Component({
  selector: 'support-message-input',
  imports: [
    CommonModule,
    FormsModule,
    TextareaModule,
    ButtonModule,
  ],
  templateUrl: './message-input.html',
  styleUrl: './message-input.css',
})
export class MessageInput implements OnDestroy {
  @Output() send = new EventEmitter<string>()
  @Output() typing = new EventEmitter<boolean>()

  text = ''
  private lastTypingSent = 0
  private readonly typingDelay = 1800 // ms

  private stopTypingTimer?: number
  private readonly stopTypingDelay = 2000 // ms

  ngOnDestroy(): void {
    if (this.stopTypingTimer) {
      clearTimeout(this.stopTypingTimer)
      this.stopTypingTimer = undefined
    }
  }

  submit() {
    const trim = this.text.trim()
    if (!trim) return
    this.send.emit(trim)
    this.text = ''
    if (this.stopTypingTimer) {
      clearTimeout(this.stopTypingTimer)
      this.stopTypingTimer = undefined
    }
    this.typing.emit(false)
  }

  maybeSend(e: KeyboardEvent) {
    if (!e.shiftKey && e.key === 'Enter') {
      e.preventDefault()
      this.submit()
    }
  }

  onInput(_: Event) {
    const now = Date.now()

    if (now - this.lastTypingSent > this.typingDelay) {
      this.emitTyping(true)
      this.lastTypingSent = now
    }

    if (this.stopTypingTimer) {
      clearTimeout(this.stopTypingTimer)
    }
    this.stopTypingTimer = window.setTimeout(() => {
      this.typing.emit(false)
      this.stopTypingTimer = undefined
    }, this.stopTypingDelay)
  }

  emitTyping(v: boolean) {
    this.typing.emit(v)
  }
}
