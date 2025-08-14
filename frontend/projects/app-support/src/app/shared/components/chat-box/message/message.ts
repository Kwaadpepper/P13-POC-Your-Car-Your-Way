import { DatePipe } from '@angular/common'
import { Component, Input } from '@angular/core'

import { AvatarModule } from 'primeng/avatar'
import { TagModule } from 'primeng/tag'

import { ChatMessage } from '~support-domains/chat/models'
import { Role } from '~support-shared/enums'

@Component({
  selector: 'support-message',
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
    const id = this.message.from.name || ''
    const part = id.split('-').pop() || id
    return (part[0] || '?').toUpperCase()
  }

  get from(): { id: string, role: Role, isOperator: boolean } {
    return {
      id: this.message.from.id,
      role: this.message.from.role,
      isOperator: this.message.from.role === Role.OPERATOR,
    }
  }
}
