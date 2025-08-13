import { CommonModule } from '@angular/common'
import { Component, inject, OnDestroy, OnInit } from '@angular/core'

import { ButtonModule } from 'primeng/button'
import { DialogModule } from 'primeng/dialog'
import { Subscription } from 'rxjs'

import { SessionBroadcastService, SessionBroadcastType } from '~ycyw/shared'

@Component({
  selector: 'support-session-listener',
  imports: [
    CommonModule,
    DialogModule,
    ButtonModule,
  ],
  templateUrl: 'session-listener.html',
  styleUrl: 'session-listener.css',
})
export class SessionListener implements OnInit, OnDestroy {
  private readonly bus = inject(SessionBroadcastService)
  private sub!: Subscription

  public showLogoutDialog = false

  ngOnInit(): void {
    this.sub = this.bus.events$.subscribe((evt) => {
      if (evt.type === SessionBroadcastType.LOGOUT) {
        this.showLogoutDialog = true
      }
      if (evt.type === SessionBroadcastType.LOGOUT) {
        this.showLogoutDialog = false
      }
    })
  }

  ngOnDestroy(): void {
    this.sub.unsubscribe()
  }

  onConfirm(): void {
    this.showLogoutDialog = false
  }
}
