import { ComponentFixture, TestBed } from '@angular/core/testing'

import { IssuePageViewModel } from './issue-page-viewmodel'

describe('IssuePageViewModel', () => {
  let component: IssuePageViewModel
  let fixture: ComponentFixture<IssuePageViewModel>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IssuePageViewModel],
    })
      .compileComponents()

    fixture = TestBed.createComponent(IssuePageViewModel)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })
})
