package com.gameboard.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gameboard.domain.entity.Game;
import com.gameboard.domain.enums.GameStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GameRepository extends JpaRepository<Game, UUID> {

    List<Game> findByStatus(GameStatus status);

    Page<Game> findByStatusAndTitleContainingIgnoreCase(
        GameStatus status, String title, Pageable pageable
    );

    List<Game> findByStatusAndCategoriesName(GameStatus status, String categoryName);

    Optional<Game> findByBggId(String bggId);
}