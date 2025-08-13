import { ComponentFixture, TestBed } from '@angular/core/testing'

import { FaqListViewModel } from './faq-list-viewmodel'

describe('FaqListViewModel', () => {
  let component: FaqListViewModel
  let fixture: ComponentFixture<FaqListViewModel>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FaqListViewModel],
    })
      .compileComponents()

    fixture = TestBed.createComponent(FaqListViewModel)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })
})
