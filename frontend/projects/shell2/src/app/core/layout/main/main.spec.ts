import { TestBed } from '@angular/core/testing'
import { RouterModule } from '@angular/router'

import { Main } from './main'

describe('Main', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterModule.forRoot([]),
      ],
      declarations: [
        Main,
      ],
    }).compileComponents()
  })

  it('should create the component', () => {
    const fixture = TestBed.createComponent(Main)
    const component = fixture.componentInstance
    expect(component).toBeTruthy()
  })
})
