import { ComponentFixture, TestBed } from '@angular/core/testing'

import { IssueListViewModel } from './issue-list-viewmodel'

describe('IssueListViewModel', () => {
  let component: IssueListViewModel
  let fixture: ComponentFixture<IssueListViewModel>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IssueListViewModel],
    })
      .compileComponents()

    fixture = TestBed.createComponent(IssueListViewModel)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })
})
