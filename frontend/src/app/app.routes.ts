import { Routes } from '@angular/router';
import { GameListComponent } from './pages/game-list/game-list';
import { GameDetailComponent } from './pages/game-detail/game-detail';
import { GameProposeComponent } from './pages/game-propose/game-propose';

export const routes: Routes = [
  { path: '', component: GameListComponent },
  { path: 'games', component: GameListComponent },
  { path: 'games/:id', component: GameDetailComponent },
  { path: 'propose', component: GameProposeComponent },
];