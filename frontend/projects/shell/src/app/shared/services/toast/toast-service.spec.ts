import { TestBed } from '@angular/core/testing'
import { MessageService } from 'primeng/api'

import { ToastService } from './toast-service'

describe('ToastService', () => {
  let service: ToastService
  let messageService: MessageService

  beforeEach(() => {
    messageService = jasmine.createSpyObj('MessageService', ['add'])

    TestBed.configureTestingModule({
      providers: [{
        provide: MessageService,
        useValue: messageService,
      }],
    })
    service = TestBed.inject(ToastService)
  })

  it('should be created', () => {
    expect(service).toBeTruthy()
  })
})
