import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GameProposeComponent } from './game-propose';

describe('GamePropose', () => {
  let component: GameProposeComponent;
  let fixture: ComponentFixture<GameProposeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GameProposeComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GameProposeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
