package com.gameboard.domain.entity;

import jakarta.persistence.*;
import java.util.UUID;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "game_category")
public class GameCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "categories")
    private Set<Game> games = new HashSet<>();

    // getters / setters
}