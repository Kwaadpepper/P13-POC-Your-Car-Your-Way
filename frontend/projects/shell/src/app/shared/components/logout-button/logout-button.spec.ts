import { ComponentFixture, TestBed } from '@angular/core/testing'
import { provideRouter } from '@angular/router'
import { ToastService } from '@shell-shared/services'
import { of } from 'rxjs'

import { LogoutButton } from './logout-button'
import { LogoutButtonViewModel } from './logout-button-viewmodel'

describe('LogoutButton', () => {
  let component: LogoutButton
  let fixture: ComponentFixture<LogoutButton>
  let viewModel: LogoutButtonViewModel
  let toastService: ToastService

  beforeEach(async () => {
    viewModel = jasmine.createSpyObj('LogoutButtonViewModel', {}, {
      logout: of(),
    })
    toastService = jasmine.createSpyObj('ToastService', ['info', 'error'])

    await TestBed.configureTestingModule({
      imports: [LogoutButton],
      providers: [
        provideRouter([]),
        { provide: LogoutButtonViewModel, useValue: viewModel },
        { provide: ToastService, useValue: toastService },
      ],
    })
      .overrideComponent(LogoutButton, {
        set: {
          providers: [
            { provide: LogoutButtonViewModel, useValue: viewModel },
          ],
        },
      })
      .compileComponents()

    fixture = TestBed.createComponent(LogoutButton)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })
})
