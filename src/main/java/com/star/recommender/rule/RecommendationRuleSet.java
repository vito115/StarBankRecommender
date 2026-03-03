package com.star.recommender.rule;

import com.star.recommender.model.Product;

import java.util.Optional;
import java.util.UUID;

public interface RecommendationRuleSet {
    Optional<Product> check(UUID userId);
    UUID getProductId();
}
