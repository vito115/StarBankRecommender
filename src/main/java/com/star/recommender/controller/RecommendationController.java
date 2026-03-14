package com.star.recommender.controller;

import com.star.recommender.dto.RecommendationResponse;
import com.star.recommender.service.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.UUID;

@RestController
@RequestMapping("/recommendation")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<RecommendationResponse> getRecommendations(
            @PathVariable("user_id") String userIdStr) {

        UUID userId;
        try {
            userId = UUID.fromString(userIdStr);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.ok(new RecommendationResponse(null, Collections.emptyList()));
        }

        var recommendations = recommendationService.getRecommendationsForUser(userId);
        var response = new RecommendationResponse(userId, recommendations);

        return ResponseEntity.ok(response);
    }
}
