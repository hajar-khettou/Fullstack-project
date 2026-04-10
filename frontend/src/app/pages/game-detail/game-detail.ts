import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { GameService } from '../../core/services/game';
import { RatingService } from '../../core/services/rating';
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
  loading: boolean = false;

  // Formulaire de notation
  newRating: Rating = {
    boardGameId: 0,
    score: 5,
    comment: ''
  };
  ratingSubmitted: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private gameService: GameService,
    private ratingService: RatingService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.newRating.boardGameId = id;
    this.loading = true;
    this.gameService.getGameById(id).subscribe({
      next: (data) => {
        this.game = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Erreur chargement jeu', err);
        this.loading = false;
      }
    });
  }

  submitRating(): void {
    this.ratingService.rateGame(this.newRating.boardGameId, this.newRating).subscribe({
      next: () => {
        this.ratingSubmitted = true;
      },
      error: (err) => console.error('Erreur notation', err)
    });
  }
}