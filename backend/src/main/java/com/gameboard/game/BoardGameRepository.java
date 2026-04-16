package com.gameboard.game;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BoardGameRepository extends JpaRepository<BoardGame, Long> {

    List<BoardGame> findByTitleContainingIgnoreCase(String title);

    List<BoardGame> findByStatus(BoardGame.Status status);

    Page<BoardGame> findByStatus(BoardGame.Status status, Pageable pageable);

    @Query("SELECT g FROM BoardGame g WHERE g.status = 'APPROVED'" +
           " AND (:genre IS NULL OR LOWER(g.genre) = LOWER(:genre))" +
           " AND (:year IS NULL OR g.year = :year)" +
           " AND (:title IS NULL OR LOWER(g.title) LIKE LOWER(CONCAT('%', :title, '%')))")
    Page<BoardGame> search(@Param("title") String title, @Param("genre") String genre,
                           @Param("year") Integer year, Pageable pageable);
}
