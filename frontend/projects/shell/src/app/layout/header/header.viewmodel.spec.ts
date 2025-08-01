import { TestBed } from '@angular/core/testing'

import { Router } from '@angular/router'
import { SessionService } from '@core/services'
import { of } from 'rxjs'
import { HeaderViewModel } from './header.viewmodel'

describe('HeaderModel', () => {
  let viewModel: HeaderViewModel
  let router: Router
  let sessionService: SessionService

  beforeEach(async () => {
    router = jasmine.createSpyObj('Router', ['navigate'], {
      events: {
        pipe: () => of(),
      },
    })
    sessionService = jasmine.createSpyObj('SessionService', ['isAuthenticated'])

    await TestBed.configureTestingModule({
      providers: [
        {
          provide: Router,
          useValue: router,
        },
        {
          provide: SessionService,
          useValue: sessionService,
        },
      ],
    })

    viewModel = TestBed.inject(HeaderViewModel)
  })

  it('should create', () => {
    expect(viewModel).toBeTruthy()
  })
})
