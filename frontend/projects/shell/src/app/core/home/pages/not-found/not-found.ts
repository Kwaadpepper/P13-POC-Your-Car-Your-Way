import { Component } from '@angular/core'

import { NotFoundViewModel } from './not-found-viewmodel'

@Component({
  selector: 'shell-not-found',
  templateUrl: './not-found.html',
  styleUrl: './not-found.css',
  providers: [NotFoundViewModel],
})
export class NotFound {
}
