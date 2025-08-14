import { TestBed } from '@angular/core/testing'

import { DashboardViewModel } from './dashboard-viewmodel'

describe('DashboardViewModel', () => {
  let viewModel: DashboardViewModel

  beforeEach(async () => {
    TestBed.configureTestingModule({
    })

    viewModel = TestBed.inject(DashboardViewModel)
  })

  it('should create', () => {
    expect(viewModel).toBeTruthy()
  })
})
