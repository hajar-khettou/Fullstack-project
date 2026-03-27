import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BoardGame } from '../../models/game.model';

@Injectable({
  providedIn: 'root',
})
export class GameService {
  private apiUrl = 'http://localhost:8080/api/games';

  constructor(private http: HttpClient) {}

  // GET /api/games → liste des jeux approuvés
  getGames(): Observable<BoardGame[]> {
    return this.http.get<BoardGame[]>(this.apiUrl);
  }

  // GET /api/games/{id} → fiche détaillée
  getGameById(id: number): Observable<BoardGame> {
    return this.http.get<BoardGame>(`${this.apiUrl}/${id}`);
  }

  // GET /api/games/search?title=xxx → recherche
  searchGames(title: string): Observable<BoardGame[]> {
    return this.http.get<BoardGame[]>(`${this.apiUrl}/search?title=${title}`);
  }

  // POST /api/games → proposer un jeu
  proposeGame(game: BoardGame): Observable<BoardGame> {
    return this.http.post<BoardGame>(this.apiUrl, game);
  }
}
