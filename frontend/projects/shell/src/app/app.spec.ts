import { TestBed } from '@angular/core/testing'
import { MessageService } from 'primeng/api'

import { App } from './app'

describe('App', () => {
  let messageService: MessageService

  beforeEach(async () => {
    messageService = jasmine.createSpyObj('MessageService', ['add'])

    await TestBed.configureTestingModule({
      imports: [App],
      providers: [{
        provide: MessageService,
        useValue: messageService,
      }],
    }).compileComponents()
  })

  it('should create the app', () => {
    const fixture = TestBed.createComponent(App)
    const app = fixture.componentInstance
    expect(app).toBeTruthy()
  })
})
