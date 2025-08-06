import { ComponentFixture, TestBed } from '@angular/core/testing'

import { signal } from '@angular/core'
import { provideRouter } from '@angular/router'
import { HomePage } from './home'
import { HomeViewModel } from './home.viewmodel'

describe('HomePage', () => {
  let component: HomePage
  let fixture: ComponentFixture<HomePage>
  let viewModel: HomeViewModel

  beforeEach(async () => {
    viewModel = jasmine.createSpyObj('HomeViewModel', {}, {
      appName: signal(''),
    })

    await TestBed.configureTestingModule({
      imports: [HomePage],
      providers: [
        provideRouter([]),
        {
          provide: HomeViewModel,
          useValue: viewModel,
        }],
    })
      .overrideComponent(HomePage, {
        set: {
          providers: [
            {
              provide: HomeViewModel,
              useValue: viewModel,
            },
          ],
        },
      })
      .compileComponents()

    fixture = TestBed.createComponent(HomePage)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })
})
