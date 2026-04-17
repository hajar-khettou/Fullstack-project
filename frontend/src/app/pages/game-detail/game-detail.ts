import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { GameService } from '../../core/services/game';
import { RatingService } from '../../core/services/rating';
import { AuthService } from '../../core/services/auth.service';
import { BoardGame } from '../../models/game.model';
import { Rating } from '../../models/rating.model';

@Component({
  selector: 'app-game-detail',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './game-detail.html',
  styleUrl: './game-detail.css'
})
export class GameDetailComponent implements OnInit {

  game: BoardGame | null = null;
  loading = false;
  ratingError = '';

  newRating: Rating = { boardGameId: 0, score: 5, comment: '' };
  ratingSubmitted = false;

  ratings: Rating[] = [];

  constructor(
    private route: ActivatedRoute,
    private gameService: GameService,
    private ratingService: RatingService,
    public authService: AuthService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.newRating.boardGameId = id;
    this.loading = true;
    this.gameService.getGameById(id).subscribe({
      next: (data) => {
        this.game = data;
        this.loading = false;
        this.ratingService.getRatings(id).subscribe({
          next: (r) => { this.ratings = r; },
          error: () => {}
        });
      },
      error: () => { this.loading = false; }
    });
  }

  submitRating(): void {
    this.ratingError = '';
    const user = this.authService.getUser();
    if (user) this.newRating.userId = user.username;

    this.ratingService.rateGame(this.newRating.boardGameId, this.newRating).subscribe({
      next: () => { this.ratingSubmitted = true; },
      error: (err) => {
        if (err.status === 401 || err.status === 403) {
          this.ratingError = 'Vous devez être connecté pour noter un jeu.';
        } else if (err.status === 409) {
          this.ratingError = 'Vous avez déjà noté ce jeu.';
        } else {
          this.ratingError = 'Une erreur est survenue, réessayez.';
        }
      }
    });
  }
}
