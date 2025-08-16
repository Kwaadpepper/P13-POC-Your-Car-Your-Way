import { TestBed } from '@angular/core/testing'
import { Router } from '@angular/router'

import { ToastService } from '@ycyw/shell-shared/services'
import { SessionStore } from '@ycyw/shell-shared/stores'

import { SessionInterceptor } from './session-interceptor'

describe('SessionInterceptor', () => {
  let rooter: Router
  let sessionStore: SessionStore
  let toastService: ToastService

  beforeEach(() => {
    rooter = jasmine.createSpyObj('Router', ['navigate'])
    sessionStore = jasmine.createSpyObj('SessionService', ['isAuthenticated'])
    toastService = jasmine.createSpyObj('ToastService', ['showError'])

    TestBed.configureTestingModule({
      providers: [
        SessionInterceptor,
        {
          provide: Router,
          useValue: rooter,
        },
        {
          provide: SessionStore,
          useValue: sessionStore,
        },
        {
          provide: ToastService,
          useValue: toastService,
        },
      ],
    })
  })

  it('should be created', () => {
    const interceptor: SessionInterceptor = TestBed.inject(SessionInterceptor)
    expect(interceptor).toBeTruthy()
  })
})
