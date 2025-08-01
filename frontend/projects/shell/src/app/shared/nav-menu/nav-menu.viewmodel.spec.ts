import { TestBed } from '@angular/core/testing'

import { SessionService } from '@core/services'
import { NavMenuViewModel } from './nav-menu.viewmodel'

describe('NavMenuViewModel', () => {
  let viewModel: NavMenuViewModel
  let sessionService: SessionService

  beforeEach(async () => {
    sessionService = jasmine.createSpyObj('SessionService', ['isAuthenticated'])

    TestBed.configureTestingModule({
      providers: [{
        provide: SessionService,
        useValue: sessionService,
      }],
    })

    viewModel = TestBed.inject(NavMenuViewModel)
  })

  it('should create', () => {
    expect(viewModel).toBeTruthy()
  })
})
