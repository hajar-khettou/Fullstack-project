import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink, Router } from '@angular/router';
import { GameService } from '../../core/services/game';
import { AuthService } from '../../core/services/auth.service';
import { BoardGame } from '../../models/game.model';

@Component({
  selector: 'app-game-propose',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './game-propose.html',
  styleUrl: './game-propose.css'
})
export class GameProposeComponent {

  game: BoardGame = {
    title: '',
    description: '',
    imageUrl: '',
    minPlayers: 2,
    maxPlayers: 4,
    year: new Date().getFullYear()
  };

  submitted = false;
  error = '';

  // ← Variables BGG ajoutées ici
  bggTitle: string = '';
  loadingBgg: boolean = false;
  bggError: string = '';
  bggSuccess: boolean = false;

  constructor(
    private gameService: GameService,
    public authService: AuthService,
    private router: Router
  ) {}

  canPropose(): boolean {
    return this.authService.hasRoleOrHigher('EDITOR');
  }

  // ← Méthode autoFill ajoutée ici
  autoFill(): void {
    if (!this.bggTitle.trim()) {
      this.bggError = 'Entrez un titre pour la recherche BGG.';
      return;
    }
    this.loadingBgg = true;
    this.bggError = '';
    this.bggSuccess = false;

    //this.gameService.importFromBgg(this.bggTitle).subscribe({
    this.gameService.proposeGame({ title: this.bggTitle }).subscribe({
      next: (data) => {
        this.game.title = data.title || this.bggTitle;
        this.game.description = data.description || '';
        this.game.imageUrl = data.imageUrl || '';
        this.game.minPlayers = data.minPlayers || 2;
        this.game.maxPlayers = data.maxPlayers || 4;
        this.game.year = data.year || new Date().getFullYear();
        this.bggSuccess = true;
        this.loadingBgg = false;
      },
      error: () => {
        this.bggError = 'Jeu introuvable sur BoardGameGeek. Remplissez manuellement.';
        this.loadingBgg = false;
      }
    });
  }

  propose(): void {
    if (!this.game.title.trim()) {
      this.error = 'Le titre est obligatoire.';
      return;
    }
    this.error = '';
    this.gameService.proposeGame(this.game).subscribe({
      next: () => {
        this.submitted = true;
        setTimeout(() => this.router.navigate(['/']), 3000);
      },
      error: (err) => {
        if (err.status === 401 || err.status === 403) {
          this.error = 'Vous devez être connecté en tant qu\'éditeur ou webmaster.';
        } else {
          this.error = 'Une erreur est survenue, réessayez.';
        }
      }
    });
  }
}