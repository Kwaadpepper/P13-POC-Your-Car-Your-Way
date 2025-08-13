import { TestBed } from '@angular/core/testing'

import { AuthService } from '~shell-core/auth/services'
import { ToastService } from '~shell-shared/services'

import { LoginViewModel } from './login-viewmodel'

describe('LoginViewModel', () => {
  let viewModel: LoginViewModel
  let authService: AuthService
  let toastService: ToastService

  beforeEach(async () => {
    authService = jasmine.createSpyObj('AuthService', ['login'])
    toastService = jasmine.createSpyObj('ToastService', ['showError'])
    TestBed.configureTestingModule({
      providers: [
        {
          provide: AuthService,
          useValue: authService,
        },
        {
          provide: ToastService,
          useValue: toastService,
        },
      ],
    })

    viewModel = TestBed.inject(LoginViewModel)
  })

  it('should create', () => {
    expect(viewModel).toBeTruthy()
  })
})
