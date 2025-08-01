import { ComponentFixture, TestBed } from '@angular/core/testing'

import { signal } from '@angular/core'
import { provideRouter } from '@angular/router'
import { HomeComponent } from './home.component'
import { HomeViewModel } from './home.viewmodel'

describe('HomeComponent', () => {
  let component: HomeComponent
  let fixture: ComponentFixture<HomeComponent>
  let viewModel: HomeViewModel

  beforeEach(async () => {
    viewModel = jasmine.createSpyObj('HomeViewModel', {}, {
      appName: signal(''),
    })

    await TestBed.configureTestingModule({
      imports: [HomeComponent],
      providers: [
        provideRouter([]),
        {
          provide: HomeViewModel,
          useValue: viewModel,
        }],
    })
      .overrideComponent(HomeComponent, {
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

    fixture = TestBed.createComponent(HomeComponent)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })
})
