import { DatePipe } from '@angular/common'
import { Component, OnInit, inject } from '@angular/core'
import { NotFoundError } from '@angular/core/primitives/di'
import { ActivatedRoute, Router } from '@angular/router'

import { ButtonModule } from 'primeng/button'
import { DividerModule } from 'primeng/divider'
import { MessageModule } from 'primeng/message'
import { ProgressSpinnerModule } from 'primeng/progressspinner'
import { TagModule } from 'primeng/tag'

import { BackButton } from '~support-shared/components'
import { UUID, uuidSchema } from '~ycyw/shared'

import { IssuePageViewModel } from './issue-page-viewmodel'

@Component({
  selector: 'support-issue-page',
  standalone: true,
  imports: [
    DatePipe,
    ButtonModule,
    TagModule,
    MessageModule,
    ProgressSpinnerModule,
    DividerModule,
    BackButton,
  ],
  providers: [IssuePageViewModel],
  templateUrl: './issue-page.html',
  styleUrl: './issue-page.css',
})
export class IssuePage implements OnInit {
  private readonly route = inject(ActivatedRoute)
  readonly router = inject(Router)
  readonly viewModel = inject(IssuePageViewModel)

  ngOnInit() {
    this.route.paramMap.subscribe(async (params) => {
      try {
        const id: UUID = uuidSchema.parse(params.get('id'))
        this.viewModel.setIssueId(id)
        if (id && !this.viewModel.issue()) {
          await this.viewModel.getIssue(id)
        }
      }
      catch {
        throw new NotFoundError('No conversation found')
      }
    })
  }

  reloadIssue() {
    this.viewModel.reload()
  }

  openChat() {
    const id = this.viewModel.issue()?.id
    if (!id) return
    this.router.navigate(['.', 'chat'], { relativeTo: this.route })
  }
}
