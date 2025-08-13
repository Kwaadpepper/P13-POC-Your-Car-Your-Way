import { ComponentFixture, TestBed } from '@angular/core/testing'

import { SessionListener } from './session-listener'

describe('SessionListener', () => {
  let component: SessionListener
  let fixture: ComponentFixture<SessionListener>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SessionListener],
    })
      .compileComponents()

    fixture = TestBed.createComponent(SessionListener)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })
})
