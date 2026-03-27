package com.gameboard.domain.repository;
// GameCategoryRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gameboard.domain.entity.GameCategory;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GameCategoryRepository extends JpaRepository<GameCategory, UUID> {

    Optional<GameCategory> findByNameIgnoreCase(String name);

    List<GameCategory> findAllByOrderByNameAsc();
}