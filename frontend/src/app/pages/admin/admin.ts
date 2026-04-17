import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { GameService } from '../../core/services/game';
import { BoardGame, Page } from '../../models/game.model';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './admin.html',
  styleUrl: './admin.css'
})
export class AdminComponent implements OnInit {

  pendingGames: BoardGame[] = [];
  approvedGames: BoardGame[] = [];
  loading = false;
  message = '';

  editingGame: BoardGame | null = null;
  gameToDelete: BoardGame | null = null;

  constructor(private gameService: GameService) {}

  ngOnInit(): void {
    this.loadPending();
    this.loadApproved();
  }

  loadPending(): void {
    this.loading = true;
    this.gameService.getPendingGames().subscribe({
      next: (data) => { this.pendingGames = data; this.loading = false; },
      error: () => { this.loading = false; }
    });
  }

  loadApproved(): void {
    this.gameService.getGames(undefined, undefined, undefined, 0, 100).subscribe({
      next: (page: Page<BoardGame>) => { this.approvedGames = page.content; },
      error: () => {}
    });
  }

  approve(game: BoardGame): void {
    this.gameService.updateStatus(game.id!, 'APPROVED').subscribe({
      next: () => {
        this.message = `"${game.title}" approuvé.`;
        this.pendingGames = this.pendingGames.filter(g => g.id !== game.id);
        this.loadApproved();
      },
      error: () => { this.message = 'Erreur lors de l\'approbation.'; }
    });
  }

  reject(game: BoardGame): void {
    this.gameService.updateStatus(game.id!, 'REJECTED').subscribe({
      next: () => {
        this.message = `"${game.title}" rejeté.`;
        this.pendingGames = this.pendingGames.filter(g => g.id !== game.id);
      },
      error: () => { this.message = 'Erreur lors du rejet.'; }
    });
  }

  startEdit(game: BoardGame): void {
    this.editingGame = { ...game };
  }

  cancelEdit(): void {
    this.editingGame = null;
  }

  saveEdit(): void {
    if (!this.editingGame) return;
    this.gameService.updateGame(this.editingGame.id!, this.editingGame).subscribe({
      next: () => {
        this.message = `"${this.editingGame!.title}" mis à jour.`;
        this.editingGame = null;
        this.loadApproved();
      },
      error: () => { this.message = 'Erreur lors de la mise à jour.'; }
    });
  }

  confirmDelete(game: BoardGame): void {
    this.gameToDelete = game;
  }

  cancelDelete(): void {
    this.gameToDelete = null;
  }

  delete(): void {
    if (!this.gameToDelete) return;
    const game = this.gameToDelete;
    this.gameToDelete = null;
    this.gameService.deleteGame(game.id!).subscribe({
      next: () => {
        this.message = `"${game.title}" supprimé.`;
        this.loadApproved();
      },
      error: () => { this.message = 'Erreur lors de la suppression.'; }
    });
  }
}
