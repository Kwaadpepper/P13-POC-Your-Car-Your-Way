import { Component, computed, input } from '@angular/core'

import { ChipModule } from 'primeng/chip'

import { UserId } from '@ycyw/support-domains/chat/models'
import { Role } from '@ycyw/support-shared/enums'

export interface TypingIndicatorUser {
  user: UserId
  role: Role
}

@Component({
  selector: 'support-typing-indicator',
  imports: [
    ChipModule,
  ],
  templateUrl: './typing-indicator.html',
  styleUrl: './typing-indicator.css',
})
export class TypingIndicator {
  readonly typingUsers = input.required<TypingIndicatorUser[]>()

  readonly label = computed(() => {
    const ops = this.typingUsers().filter(u => u.role === Role.OPERATOR)
    const cli = this.typingUsers().filter(u => u.role === Role.CLIENT)
    if (ops.length && cli.length) return 'Plusieurs personnes écrivent…'
    if (ops.length) return ops.length === 1 ? 'Un opérateur écrit…' : 'Des opérateurs écrivent…'
    if (cli.length) return cli.length === 1 ? 'Le client écrit…' : 'Des clients écrivent…'
    return ''
  })
}
