import { signal } from '@angular/core'
import { ComponentFixture, TestBed } from '@angular/core/testing'
import { provideRouter } from '@angular/router'
import { ToastService } from '@shell-shared/services'

import { Login } from './login'
import { LoginViewModel } from './login-viewmodel'

describe('LoginComponent', () => {
  let component: Login
  let fixture: ComponentFixture<Login>
  let viewModel: LoginViewModel
  let toastService: ToastService

  beforeEach(async () => {
    viewModel = jasmine.createSpyObj('LoginViewModel', {}, {
      formErrorMessage: signal(''),
      loading: signal(false),
    })
    toastService = jasmine.createSpyObj('ToastService', ['error'])

    await TestBed.configureTestingModule({
      imports: [Login],
      providers: [
        provideRouter([]),
        {
          provide: LoginViewModel,
          useValue: viewModel,
        },
        {
          provide: ToastService,
          useValue: toastService,
        },
      ],
    })
      .overrideComponent(Login, {
        set: {
          providers: [
            {
              provide: LoginViewModel,
              useValue: viewModel,
            },
          ],
        },
      })
      .compileComponents()

    fixture = TestBed.createComponent(Login)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })
})
