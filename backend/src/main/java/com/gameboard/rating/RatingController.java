package com.gameboard.rating;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/games/{gameId}/ratings")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RatingController {

    private final RatingService service;

    @GetMapping
    public List<Rating> getRatings(@PathVariable Long gameId) {
        return service.getRatingsForGame(gameId);
    }

    @PostMapping
    public ResponseEntity<Rating> addRating(
            @PathVariable Long gameId,
            @RequestBody Rating rating) {
        return ResponseEntity.ok(service.addRating(gameId, rating));
    }
}
