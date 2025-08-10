import { DatePipe } from '@angular/common'
import { Component, Input } from '@angular/core'

import { AvatarModule } from 'primeng/avatar'
import { TagModule } from 'primeng/tag'

import { Role } from '~support-domains/chat/enums'
import { ChatMessage } from '~support-domains/chat/models'

@Component({
  selector: 'support-message',
  standalone: true,
  imports: [
    AvatarModule,
    TagModule,
    DatePipe,
  ],
  templateUrl: './message.html',
  styleUrl: './message.css',
})
export class Message {
  @Input({ required: true }) message!: ChatMessage
  @Input() self = false

  get initials(): string {
    const id = this.message.from.id || ''
    const part = id.split('-').pop() || id
    return (part[0] || '?').toUpperCase()
  }

  get from(): { id: string, role: string, isOperator: boolean } {
    return {
      id: this.message.from.id,
      role: this.message.from.role,
      isOperator: this.message.from.role === Role.OPERATOR,
    }
  }
}
