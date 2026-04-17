import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { GameService } from '../../core/services/game';
import { BoardGame, Page } from '../../models/game.model';

@Component({
  selector: 'app-game-list',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './game-list.html',
  styleUrl: './game-list.css'
})
export class GameListComponent implements OnInit {

  games: BoardGame[] = [];
  loading = false;

  searchTitle = '';
  searchGenre = '';
  searchYear: number | undefined;

  currentPage = 0;
  totalPages = 0;
  totalElements = 0;
  pageSize = 12;

  constructor(private gameService: GameService) {}

  ngOnInit(): void {
    this.loadGames();
  }

  loadGames(): void {
    this.loading = true;
    this.gameService.getGames(this.searchTitle || undefined, this.searchGenre || undefined, this.searchYear, this.currentPage, this.pageSize).subscribe({
      next: (page: Page<BoardGame>) => {
        this.games = page.content;
        this.totalPages = page.totalPages;
        this.totalElements = page.totalElements;
        this.loading = false;
      },
      error: () => { this.loading = false; }
    });
  }

  search(): void {
    this.currentPage = 0;
    this.loadGames();
  }

  goToPage(page: number): void {
    if (page < 0 || page >= this.totalPages) return;
    this.currentPage = page;
    this.loadGames();
  }

  get pages(): number[] {
    return Array.from({ length: this.totalPages }, (_, i) => i);
  }
}
