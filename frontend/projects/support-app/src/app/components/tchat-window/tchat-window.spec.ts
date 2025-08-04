import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TchatWindow } from './tchat-window';

describe('TchatWindow', () => {
  let component: TchatWindow;
  let fixture: ComponentFixture<TchatWindow>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TchatWindow]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TchatWindow);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
