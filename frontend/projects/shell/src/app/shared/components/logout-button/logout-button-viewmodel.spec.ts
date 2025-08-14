import { TestBed } from '@angular/core/testing'

import { LogoutButtonViewModel } from './logout-button-viewmodel'

describe('LogoutButtonViewModel', () => {
  let viewModel: LogoutButtonViewModel

  beforeEach(async () => {
    TestBed.configureTestingModule({
    })

    viewModel = TestBed.inject(LogoutButtonViewModel)
  })

  it('should create', () => {
    expect(viewModel).toBeTruthy()
  })
})
