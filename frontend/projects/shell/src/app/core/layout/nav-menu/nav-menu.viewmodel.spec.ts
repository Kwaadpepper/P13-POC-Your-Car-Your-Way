import { TestBed } from '@angular/core/testing'

import { SessionStore } from '@ycyw/shell-core/auth/stores'

import { NavMenuViewModel } from './nav-menu.viewmodel'

describe('NavMenuViewModel', () => {
  let viewModel: NavMenuViewModel
  let sessionStore: SessionStore

  beforeEach(async () => {
    sessionStore = jasmine.createSpyObj('SessionStore', ['isAuthenticated'])

    TestBed.configureTestingModule({
      providers: [{
        provide: SessionStore,
        useValue: sessionStore,
      }],
    })

    viewModel = TestBed.inject(NavMenuViewModel)
  })

  it('should create', () => {
    expect(viewModel).toBeTruthy()
  })
})
