import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GamePropose } from './game-propose';

describe('GamePropose', () => {
  let component: GamePropose;
  let fixture: ComponentFixture<GamePropose>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GamePropose]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GamePropose);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
