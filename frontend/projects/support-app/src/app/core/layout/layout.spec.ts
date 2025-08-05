import { TestBed } from '@angular/core/testing'
import { RouterModule } from '@angular/router'
import { Layout } from './layout'

describe('Layout', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterModule.forRoot([]),
      ],
      declarations: [
        Layout,
      ],
    }).compileComponents()
  })

  it('should create the component', () => {
    const fixture = TestBed.createComponent(Layout)
    const component = fixture.componentInstance
    expect(component).toBeTruthy()
  })
})
