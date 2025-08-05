import { ComponentFixture, TestBed } from '@angular/core/testing'

import { TchatDetail } from './tchat-detail'

describe('TchatDetail', () => {
  let component: TchatDetail
  let fixture: ComponentFixture<TchatDetail>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TchatDetail],
    })
      .compileComponents()

    fixture = TestBed.createComponent(TchatDetail)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })
})
