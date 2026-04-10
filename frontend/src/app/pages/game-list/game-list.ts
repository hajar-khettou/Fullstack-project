import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { GameService } from '../../core/services/game';
import { BoardGame } from '../../models/game.model';

@Component({
  selector: 'app-game-list',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './game-list.html',
  styleUrl: './game-list.css'
})
export class GameListComponent implements OnInit {

  games: BoardGame[] = [];
  searchTerm: string = '';
  loading: boolean = false;

  constructor(private gameService: GameService) {}

  ngOnInit(): void {
    this.loadGames();
  }

  loadGames(): void {
    this.loading = true;
    this.gameService.getGames().subscribe({
      next: (data) => {
        this.games = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Erreur chargement jeux', err);
        this.loading = false;
      }
    });
  }

  search(): void {
    if (this.searchTerm.trim() === '') {
      this.loadGames();
      return;
    }
    this.gameService.searchGames(this.searchTerm).subscribe({
      next: (data) => this.games = data,
      error: (err) => console.error('Erreur recherche', err)
    });
  }
}