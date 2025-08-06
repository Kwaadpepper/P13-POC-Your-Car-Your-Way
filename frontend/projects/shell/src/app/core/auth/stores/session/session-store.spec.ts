import { TestBed } from '@angular/core/testing'

import { SessionStore } from './session-store'

describe('SessionService', () => {
  let service: SessionStore

  beforeEach(() => {
    TestBed.configureTestingModule({})
    service = TestBed.inject(SessionStore)
  })

  it('should be created', () => {
    expect(service).toBeTruthy()
  })
})
