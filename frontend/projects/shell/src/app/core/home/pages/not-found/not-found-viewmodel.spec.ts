import { signal } from '@angular/core'
import { ComponentFixture, TestBed } from '@angular/core/testing'

import { NotFound } from './not-found'
import { NotFoundViewModel } from './not-found-viewmodel'

describe('NotFoundComponent', () => {
  let component: NotFound
  let fixture: ComponentFixture<NotFound>
  let viewModel: NotFoundViewModel

  beforeEach(async () => {
    viewModel = jasmine.createSpyObj('NotFoundViewModel', {}, {
      appName: signal(''),
    })
    await TestBed.configureTestingModule({
      imports: [NotFound],
    })
      .overrideComponent(NotFound, {
        set: {
          providers: [
            {
              provide: NotFoundViewModel,
              useValue: viewModel,
            },
          ],
        },
      })
      .compileComponents()

    fixture = TestBed.createComponent(NotFound)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })
})
