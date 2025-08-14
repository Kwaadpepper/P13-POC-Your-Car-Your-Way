import { TestBed } from '@angular/core/testing'

import { ConversationViewModel } from './conversation-viewmodel'

describe('ConversationViewModel', () => {
  let viewModel: ConversationViewModel

  beforeEach(async () => {
    TestBed.configureTestingModule({
    })

    viewModel = TestBed.inject(ConversationViewModel)
  })

  it('should create', () => {
    expect(viewModel).toBeTruthy()
  })
})
