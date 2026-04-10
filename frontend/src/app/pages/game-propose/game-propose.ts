import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink, Router } from '@angular/router';
import { GameService } from '../../core/services/game';
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

  submitted: boolean = false;
  error: string = '';

  constructor(
    private gameService: GameService,
    private router: Router
  ) {}

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
        console.error('Erreur proposition', err);
        this.error = 'Une erreur est survenue, réessayez.';
      }
    });
  }
}