package com.gameboard.rating;

import com.gameboard.game.BoardGame;
import com.gameboard.game.BoardGameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final BoardGameRepository boardGameRepository;

    public List<Rating> getRatingsForGame(Long gameId) {
        return ratingRepository.findByBoardGameId(gameId);
    }

    @Transactional
    public Rating addRating(Long gameId, Rating rating) {
        BoardGame game = boardGameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Jeu introuvable : " + gameId));

        if (game.getStatus() != BoardGame.Status.APPROVED) {
            throw new IllegalStateException("Impossible de noter un jeu non approuvé");
        }

        if (rating.getUserId() != null) {
            ratingRepository.findByBoardGameIdAndUserId(gameId, rating.getUserId())
                    .ifPresent(e -> { throw new IllegalStateException("Vous avez déjà noté ce jeu"); });
        }

        rating.setBoardGame(game);
        Rating saved = ratingRepository.save(rating);

        ratingRepository.calculateAverageByGameId(gameId)
                .ifPresent(avg -> {
                    game.setAverageRating(Math.round(avg * 10.0) / 10.0);
                    boardGameRepository.save(game);
                });

        return saved;
    }
}
