package com.gameboard.game;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BoardGameController {

    private final BoardGameService service;

    @GetMapping
    public Page<BoardGame> getAll(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Integer year,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        return service.getApprovedGames(title, genre, year,
                PageRequest.of(page, size, Sort.by("title").ascending()));
    }

    @GetMapping("/pending")
    public List<BoardGame> getPending() {
        return service.getPendingGames();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardGame> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<BoardGame> create(@RequestBody BoardGame game) {
        return ResponseEntity.ok(service.create(game));
    }

    @PostMapping("/bgg/{bggId}")
    public ResponseEntity<BoardGame> importFromBgg(@PathVariable String bggId) {
        return ResponseEntity.ok(service.importFromBgg(bggId));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<BoardGame> updateStatus(
            @PathVariable Long id,
            @RequestParam BoardGame.Status status) {
        return ResponseEntity.ok(service.updateStatus(id, status));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoardGame> update(
            @PathVariable Long id,
            @RequestBody BoardGame game) {
        return ResponseEntity.ok(service.update(id, game));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
