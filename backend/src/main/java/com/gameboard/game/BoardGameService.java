package com.gameboard.game;

import com.gameboard.bgg.BggClient;
import com.gameboard.bgg.BggGameDto;
import com.gameboard.kafka.GameEventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardGameService {

    private final BoardGameRepository repository;
    private final BggClient bggClient;
    private final Optional<GameEventProducer> kafkaProducer;

    public Page<BoardGame> getApprovedGames(String title, String genre, Integer year, Pageable pageable) {
        return repository.search(title, genre, year, pageable);
    }

    public List<BoardGame> getPendingGames() {
        return repository.findByStatus(BoardGame.Status.PENDING);
    }

    public BoardGame getById(Long id) {
        if (id == null) throw new RuntimeException("L'identifiant ne peut pas être null");
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Jeu introuvable : " + id));
    }

    public List<BoardGame> searchByTitle(String title) {
        return repository.findByTitleContainingIgnoreCase(title);
    }

    public BoardGame create(BoardGame game) {
        game.setStatus(BoardGame.Status.PENDING);
        enrichFromBgg(game);
        BoardGame saved = repository.save(game);
        Long savedId = saved.getId();
        if (savedId != null) {
            kafkaProducer.ifPresent(p -> p.sendGameImported(savedId, saved.getTitle(), "editor"));
        }
        return saved;
    }

    public BoardGame importFromBgg(String bggId) {
        BggGameDto dto = bggClient.fetchByBggId(bggId)
                .orElseThrow(() -> new RuntimeException("Jeu introuvable sur BGG : " + bggId));
        BoardGame game = new BoardGame();
        game.setTitle(dto.getTitle());
        game.setDescription(dto.getDescription());
        game.setImageUrl(dto.getImageUrl());
        game.setMinPlayers(dto.getMinPlayers());
        game.setMaxPlayers(dto.getMaxPlayers());
        game.setYear(dto.getYear());
        game.setStatus(BoardGame.Status.APPROVED);
        BoardGame saved = repository.save(game);
        Long savedId = saved.getId();
        if (savedId != null) {
            kafkaProducer.ifPresent(p -> p.sendGameImported(savedId, saved.getTitle(), "manual"));
        }
        return saved;
    }

    public BoardGame updateStatus(Long id, BoardGame.Status status) {
        BoardGame game = getById(id);
        game.setStatus(status);
        return repository.save(game);
    }

    public BoardGame update(Long id, BoardGame updated) {
        BoardGame game = getById(id);
        game.setTitle(updated.getTitle());
        game.setDescription(updated.getDescription());
        game.setImageUrl(updated.getImageUrl());
        game.setGenre(updated.getGenre());
        game.setMinPlayers(updated.getMinPlayers());
        game.setMaxPlayers(updated.getMaxPlayers());
        game.setYear(updated.getYear());
        return repository.save(game);
    }

    public void delete(Long id) {
        if (id != null) repository.deleteById(id);
    }

    private void enrichFromBgg(BoardGame game) {
        bggClient.searchBggId(game.getTitle()).flatMap(bggClient::fetchByBggId).ifPresent(dto -> {
            if (game.getDescription() == null || game.getDescription().isBlank())
                game.setDescription(dto.getDescription());
            if (game.getImageUrl() == null || game.getImageUrl().isBlank())
                game.setImageUrl(dto.getImageUrl());
            if (game.getMinPlayers() == null) game.setMinPlayers(dto.getMinPlayers());
            if (game.getMaxPlayers() == null) game.setMaxPlayers(dto.getMaxPlayers());
            if (game.getYear() == null) game.setYear(dto.getYear());
        });
    }
}
