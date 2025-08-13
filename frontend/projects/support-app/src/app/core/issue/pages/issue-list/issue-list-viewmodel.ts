import { Inject, signal } from '@angular/core'

import { IssueStatus } from '~support-domains/issue/enums'
import { Issue } from '~support-domains/issue/models'
import { AcrissCode } from '~ycyw/shared'

@Inject({
  providedIn: 'root',
})
export class IssueListViewModel {
  readonly _issues = signal<Issue[]>([])

  readonly issues = this._issues.asReadonly()

  constructor() {
    this._issues.set([
      {
        id: '7b8e42ff-4471-429d-9f1a-cb3b220cdb17',
        subject: 'Support Request 1',
        client: {
          first_name: 'Super',
          last_name: 'Client',
          email: 'client@example.net',
          phone: '+33123456789',
          birthdate: new Date('1990-01-01'),
          address: {
            line1: '123 Main St',
            city: 'Paris',
            post_code: '75001',
            country: 'France',
          },
        },
        description: 'I need help with my account.',
        status: IssueStatus.OPENNED,
        reservation: {
          status: 'confirmed',
          from: {
            agency: {
              label: 'Agency Name',
              phone: '+33123456789',
              email: 'agency@example.net',
              address: {
                line1: '123 Main St',
                city: 'Paris',
                post_code: '75001',
                country: 'France',
              },
              coordinates: {
                lat: 48.8566,
                long: 2.3522,
              },
            },
            at: new Date('2023-10-01T10:00:00Z'),
          },
          to: {
            agency: {
              label: 'Agency Name',
              phone: '+33123456789',
              email: 'agency@example.net',
              address: {
                line1: '123 Main St',
                city: 'Paris',
                post_code: '75001',
                country: 'France',
              },
              coordinates: {
                lat: 48.8566,
                long: 2.3522,
              },
            },
            at: new Date('2023-10-01T12:00:00Z'),
          },
          vehicle: { category: AcrissCode.fromCode('CDMR') },
          payment: 'accepted',
        },
        updatedAt: new Date('2023-10-01T12:00:00Z'),
      },
      {
        id: '8c9f3b2d-5a1e-4c3b-8f1a-dc3b220cdb18',
        subject: 'Support Request 2',
        client: {
          first_name: 'Another',
          last_name: 'Client',
          email: 'user@example.net',
          phone: '+33123456780',
          birthdate: new Date('1992-02-02'),
          address: {
            line1: '456 Another St',
            city: 'Lyon',
            post_code: '69001',
            country: 'France',
          },
        },
        description: 'I have an issue with my booking.',
        status: IssueStatus.CLOSED,
        reservation: {
          status: 'cancelled',
          from: {
            agency: {
              label: 'Another Agency',
              phone: '+33123456780',
              email: 'agency@example.net',
              address: {
                line1: '456 Another St',
                city: 'Lyon',
                post_code: '69001',
                country: 'France',
              },
              coordinates: {
                lat: 45.7640,
                long: 4.8357,
              },
            },
            at: new Date('2023-10-02T11:00:00Z'),
          },
          to: {
            agency: {
              label: 'Another Agency',
              phone: '+33123456780',
              email: 'agency2@example.net',
              address: {
                line1: '789 Different St',
                city: 'Marseille',
                post_code: '13001',
                country: 'France',
              },
              coordinates: {
                lat: 43.2965,
                long: 5.3698,
              },
            },
            at: new Date('2023-10-02T13:00:00Z'),
          },
          vehicle: { category: AcrissCode.fromCode('EDMR') },
          payment: 'refunded',
        },
        updatedAt: new Date('2023-10-02T14:00:00Z'),
      },
    ])
  }
}
