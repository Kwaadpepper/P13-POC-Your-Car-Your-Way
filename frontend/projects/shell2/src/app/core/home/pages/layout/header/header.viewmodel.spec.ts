import { TestBed } from '@angular/core/testing'

import { Router } from '@angular/router'
import { SessionStore } from '@shell2-core/auth/stores'
import { of } from 'rxjs'
import { HeaderViewModel } from './header.viewmodel'

describe('HeaderModel', () => {
  let viewModel: HeaderViewModel
  let router: Router
  let sessionStore: SessionStore

  beforeEach(async () => {
    router = jasmine.createSpyObj('Router', ['navigate'], {
      events: {
        pipe: () => of(),
      },
    })
    sessionStore = jasmine.createSpyObj('SessionStore', ['isAuthenticated'])

    TestBed.configureTestingModule({
      providers: [
        {
          provide: Router,
          useValue: router,
        },
        {
          provide: SessionStore,
          useValue: sessionStore,
        },
      ],
    })

    viewModel = TestBed.inject(HeaderViewModel)
  })

  it('should create', () => {
    expect(viewModel).toBeTruthy()
  })
})
