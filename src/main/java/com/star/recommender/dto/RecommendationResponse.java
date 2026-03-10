package com.star.recommender.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.star.recommender.model.Product;

import java.util.List;
import java.util.UUID;

public class RecommendationResponse {

    @JsonProperty("user_id")
    private UUID userId;

    private List<Product> recommendations;

    public RecommendationResponse() {
    }

    public RecommendationResponse(UUID userId, List<Product> recommendations) {
        this.userId = userId;
        this.recommendations = recommendations;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public List<Product> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<Product> recommendations) {
        this.recommendations = recommendations;
    }
}
