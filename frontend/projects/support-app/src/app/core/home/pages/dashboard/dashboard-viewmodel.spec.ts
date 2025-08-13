import { ComponentFixture, TestBed } from '@angular/core/testing'

import { DashboardViewModel } from './dashboard-viewmodel'

describe('DashboardViewModel', () => {
  let component: DashboardViewModel
  let fixture: ComponentFixture<DashboardViewModel>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DashboardViewModel],
    })
      .compileComponents()

    fixture = TestBed.createComponent(DashboardViewModel)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })
})
