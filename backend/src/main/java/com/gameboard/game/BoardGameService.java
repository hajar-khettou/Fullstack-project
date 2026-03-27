package com.gameboard.game;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardGameService {

    private final BoardGameRepository repository;

    // Récupérer tous les jeux approuvés (catalogue public)
    public List<BoardGame> getApprovedGames() {
        return repository.findByStatus(BoardGame.Status.APPROVED);
    }

    // Récupérer un jeu par son id
    public BoardGame getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Jeu introuvable : " + id));
    }

    // Rechercher par titre
    public List<BoardGame> searchByTitle(String title) {
        return repository.findByTitleContainingIgnoreCase(title);
    }

    // Créer un nouveau jeu (status PENDING par défaut)
    public BoardGame create(BoardGame game) {
        game.setStatus(BoardGame.Status.PENDING);
        return repository.save(game);
    }

    // Valider ou refuser un jeu (action Webmaster)
    public BoardGame updateStatus(Long id, BoardGame.Status status) {
        BoardGame game = getById(id);
        game.setStatus(status);
        return repository.save(game);
    }

    // Modifier un jeu existant
    public BoardGame update(Long id, BoardGame updated) {
        BoardGame game = getById(id);
        game.setTitle(updated.getTitle());
        game.setDescription(updated.getDescription());
        game.setImageUrl(updated.getImageUrl());
        game.setMinPlayers(updated.getMinPlayers());
        game.setMaxPlayers(updated.getMaxPlayers());
        game.setYear(updated.getYear());
        return repository.save(game);
    }

    // Supprimer un jeu
    public void delete(Long id) {
        repository.deleteById(id);
    }
}