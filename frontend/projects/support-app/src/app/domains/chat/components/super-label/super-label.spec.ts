import { ComponentFixture, TestBed } from '@angular/core/testing'

import { SuperLabel } from './super-label'

describe('SuperLabel', () => {
  let component: SuperLabel
  let fixture: ComponentFixture<SuperLabel>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SuperLabel],
    })
      .compileComponents()

    fixture = TestBed.createComponent(SuperLabel)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })
})
