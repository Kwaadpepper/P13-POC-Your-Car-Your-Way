import { CommonModule } from '@angular/common'
import { Component, EventEmitter, Output } from '@angular/core'
import { FormsModule } from '@angular/forms'

import { ButtonModule } from 'primeng/button'
import { TextareaModule } from 'primeng/textarea'

@Component({
  selector: 'support-message-input',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    TextareaModule,
    ButtonModule,
  ],
  templateUrl: './message-input.html',
  styleUrl: './message-input.css',
})
export class MessageInput {
  @Output() send = new EventEmitter<string>()
  @Output() typing = new EventEmitter<boolean>()

  text = ''
  private lastTypingSent = 0

  submit() {
    const trim = this.text.trim()
    if (!trim) return
    this.send.emit(trim)
    this.text = ''
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
    if (now - this.lastTypingSent > 800) {
      this.emitTyping(true)
      this.lastTypingSent = now
      setTimeout(() => this.typing.emit(false), 2000)
    }
  }

  emitTyping(v: boolean) {
    this.typing.emit(v)
  }
}
