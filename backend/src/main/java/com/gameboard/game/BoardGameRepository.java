package com.gameboard.game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BoardGameRepository extends JpaRepository<BoardGame, Long> {

    // Recherche par titre (contient le mot, insensible à la casse)
    List<BoardGame> findByTitleContainingIgnoreCase(String title);

    // Filtre par status (PENDING, APPROVED, REJECTED)
    List<BoardGame> findByStatus(BoardGame.Status status);
}