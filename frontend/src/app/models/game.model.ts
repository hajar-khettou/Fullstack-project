export interface BoardGame {
  id?: number;
  title: string;
  description?: string;
  imageUrl?: string;
  minPlayers?: number;
  maxPlayers?: number;
  year?: number;
  averageRating?: number;
  status?: 'PENDING' | 'APPROVED' | 'REJECTED';
}