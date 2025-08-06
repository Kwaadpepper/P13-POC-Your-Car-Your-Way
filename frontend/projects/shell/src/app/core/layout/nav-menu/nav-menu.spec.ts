import { ComponentFixture, TestBed } from '@angular/core/testing'
import { signal } from '@angular/core'

import { NavMenu } from './nav-menu'
import { NavMenuViewModel } from './nav-menu.viewmodel'

describe('NavMenuComponent', () => {
  let component: NavMenu
  let fixture: ComponentFixture<NavMenu>
  let viewModel: NavMenuViewModel

  beforeEach(async () => {
    viewModel = jasmine.createSpyObj('NavMenuViewModel', {}, {
      loggedIn: signal(false),
    })
    await TestBed.configureTestingModule({
      imports: [NavMenu],
      providers: [{
        provide: NavMenuViewModel,
        useValue: viewModel,
      }],
    })
      .overrideComponent(NavMenu, {
        set: {
          providers: [{
            provide: NavMenuViewModel,
            useValue: viewModel,
          }],
        },
      })
      .compileComponents()

    fixture = TestBed.createComponent(NavMenu)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })
})
