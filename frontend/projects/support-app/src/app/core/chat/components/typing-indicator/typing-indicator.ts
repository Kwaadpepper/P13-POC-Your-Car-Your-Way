import { Component, Input } from '@angular/core'

import { ChipModule } from 'primeng/chip'

import { Role } from '~support-domains/chat/enums'
import { UserId } from '~support-domains/chat/models'

@Component({
  selector: 'support-typing-indicator',
  standalone: true,
  imports: [
    ChipModule,
  ],
  templateUrl: './typing-indicator.html',
  styleUrl: './typing-indicator.css',
})
export class TypingIndicator {
  @Input() typingUsers: { user: UserId, role: Role }[] = []

  get label(): string {
    const ops = this.typingUsers.filter(u => u.role === Role.OPERATOR)
    const cli = this.typingUsers.filter(u => u.role === Role.CLIENT)
    if (ops.length && cli.length) return 'Plusieurs personnes écrivent…'
    if (ops.length) return ops.length === 1 ? 'Un opérateur écrit…' : 'Des opérateurs écrivent…'
    if (cli.length) return cli.length === 1 ? 'Le client écrit…' : 'Des clients écrivent…'
    return ''
  }
}
