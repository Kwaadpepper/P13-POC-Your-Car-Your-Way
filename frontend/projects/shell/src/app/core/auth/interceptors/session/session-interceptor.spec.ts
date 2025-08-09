import { TestBed } from '@angular/core/testing'
import { Router } from '@angular/router'

import { AuthService } from '~shell-core/auth/services'
import { SessionStore } from '~shell-core/auth/stores'
import { ToastService } from '~shell-shared/services'

import { SessionInterceptor } from './session-interceptor'

describe('SessionInterceptor', () => {
  let rooter: Router
  let authService: AuthService
  let sessionStore: SessionStore
  let toastService: ToastService

  beforeEach(() => {
    rooter = jasmine.createSpyObj('Router', ['navigate'])
    authService = jasmine.createSpyObj('AuthService', ['logout'])
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
          provide: AuthService,
          useValue: authService,
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
