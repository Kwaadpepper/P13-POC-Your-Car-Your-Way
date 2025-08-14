import { ComponentFixture, TestBed } from '@angular/core/testing'

import { ConversationListViewModel } from './conversation-list-viewmodel'

describe('ConversationListViewModel', () => {
  let component: ConversationListViewModel
  let fixture: ComponentFixture<ConversationListViewModel>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConversationListViewModel],
    })
      .compileComponents()

    fixture = TestBed.createComponent(ConversationListViewModel)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })
})
