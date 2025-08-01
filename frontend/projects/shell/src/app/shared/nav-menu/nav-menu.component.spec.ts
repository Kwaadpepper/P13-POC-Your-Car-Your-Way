import { ComponentFixture, TestBed } from '@angular/core/testing'

import { signal } from '@angular/core'
import { NavMenuComponent } from './nav-menu.component'
import { NavMenuViewModel } from './nav-menu.viewmodel'

describe('NavMenuComponent', () => {
  let component: NavMenuComponent
  let fixture: ComponentFixture<NavMenuComponent>
  let viewModel: NavMenuViewModel

  beforeEach(async () => {
    viewModel = jasmine.createSpyObj('NavMenuViewModel', {}, {
      loggedIn: signal(false),
    })
    await TestBed.configureTestingModule({
      imports: [NavMenuComponent],
      providers: [{
        provide: NavMenuViewModel,
        useValue: viewModel,
      }],
    })
      .overrideComponent(NavMenuComponent, {
        set: {
          providers: [{
            provide: NavMenuViewModel,
            useValue: viewModel,
          }],
        },
      })
      .compileComponents()

    fixture = TestBed.createComponent(NavMenuComponent)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })
})
