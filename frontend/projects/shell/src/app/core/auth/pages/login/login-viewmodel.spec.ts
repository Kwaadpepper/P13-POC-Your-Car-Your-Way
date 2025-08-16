import { TestBed } from '@angular/core/testing'

import { ToastService } from '@ycyw/shell-shared/services'

import { LoginViewModel } from './login-viewmodel'

describe('LoginViewModel', () => {
  let viewModel: LoginViewModel
  let toastService: ToastService

  beforeEach(async () => {
    toastService = jasmine.createSpyObj('ToastService', ['showError'])
    TestBed.configureTestingModule({
      providers: [
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
