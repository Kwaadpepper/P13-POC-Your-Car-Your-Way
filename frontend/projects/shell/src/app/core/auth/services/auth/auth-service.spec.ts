import { provideHttpClient } from '@angular/common/http'
import { provideHttpClientTesting } from '@angular/common/http/testing'
import { TestBed } from '@angular/core/testing'

import { SessionStore } from '~shell-core/auth/stores'

import { AuthService } from './auth-service'

describe('AuthService', () => {
  let service: AuthService
  let sessionStore: SessionStore

  beforeEach(() => {
    sessionStore = jasmine.createSpyObj('SessionStore', ['setLoggedIn', 'setLoggedOut'])

    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        {
          provide: SessionStore,
          useValue: sessionStore,
        }],
    })
    service = TestBed.inject(AuthService)
  })

  it('should be created', () => {
    expect(service).toBeTruthy()
  })
})
