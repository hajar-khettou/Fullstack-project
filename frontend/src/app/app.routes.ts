import { Routes } from '@angular/router';
import { GameListComponent } from './pages/game-list/game-list';
import { GameDetailComponent } from './pages/game-detail/game-detail';
import { GameProposeComponent } from './pages/game-propose/game-propose';
import { LoginComponent } from './pages/login/login';
import { AdminComponent } from './pages/admin/admin';
import { MyProposalsComponent } from './pages/my-proposals/my-proposals';

export const routes: Routes = [
  { path: '', component: GameListComponent },
  { path: 'games/:id', component: GameDetailComponent },
  { path: 'propose', component: GameProposeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'admin', component: AdminComponent },
  { path: 'my-proposals', component: MyProposalsComponent },
  { path: '**', redirectTo: '' }
];
