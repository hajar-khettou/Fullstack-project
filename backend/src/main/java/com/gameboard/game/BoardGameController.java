package com.gameboard.game;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // permet les appels depuis Angular
public class BoardGameController {

    private final BoardGameService service;

    // GET /api/games → liste des jeux approuvés
    @GetMapping
    public List<BoardGame> getAll() {
        return service.getApprovedGames();
    }

    // GET /api/games/{id} → fiche détaillée
    @GetMapping("/{id}")
    public ResponseEntity<BoardGame> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // GET /api/games/search?title=catan → recherche par titre
    @GetMapping("/search")
    public List<BoardGame> search(@RequestParam String title) {
        return service.searchByTitle(title);
    }

    // POST /api/games → proposer un nouveau jeu (éditeur)
    @PostMapping
    public ResponseEntity<BoardGame> create(@RequestBody BoardGame game) {
        return ResponseEntity.ok(service.create(game));
    }

    // PATCH /api/games/{id}/status → valider/refuser (webmaster)
    @PatchMapping("/{id}/status")
    public ResponseEntity<BoardGame> updateStatus(
            @PathVariable Long id,
            @RequestParam BoardGame.Status status) {
        return ResponseEntity.ok(service.updateStatus(id, status));
    }

    // PUT /api/games/{id} → modifier un jeu (webmaster)
    @PutMapping("/{id}")
    public ResponseEntity<BoardGame> update(
            @PathVariable Long id,
            @RequestBody BoardGame game) {
        return ResponseEntity.ok(service.update(id, game));
    }

    // DELETE /api/games/{id} → supprimer un jeu (webmaster)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}