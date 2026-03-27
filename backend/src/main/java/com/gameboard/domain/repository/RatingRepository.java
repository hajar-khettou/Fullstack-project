package com.gameboard.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.gameboard.domain.entity.Rating;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RatingRepository extends JpaRepository<Rating, UUID> {

    List<Rating> findByGameId(UUID gameId);

    Optional<Rating> findByUserIdAndGameId(UUID userId, UUID gameId);

    boolean existsByUserIdAndGameId(UUID userId, UUID gameId);

    @Query("SELECT AVG(r.score) FROM Rating r WHERE r.game.id = :gameId")
    Optional<Double> findAverageScoreByGameId(@Param("gameId") UUID gameId);
}