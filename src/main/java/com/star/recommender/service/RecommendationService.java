package com.star.recommender.service;

import com.star.recommender.dto.RuleDto;
import com.star.recommender.model.Product;
import com.star.recommender.rule.DynamicRuleExecutor;
import com.star.recommender.rule.RecommendationRuleSet;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RecommendationService {

    private final List<RecommendationRuleSet> staticRules;
    private final DynamicRuleService dynamicRuleService;
    private final DynamicRuleExecutor dynamicRuleExecutor;

    public RecommendationService(List<RecommendationRuleSet> rules,
                                 DynamicRuleService dynamicRuleService,
                                 DynamicRuleExecutor dynamicRuleExecutor) {
        this.staticRules = rules;
        this.dynamicRuleService = dynamicRuleService;
        this.dynamicRuleExecutor = dynamicRuleExecutor;
    }

    public List<Product> getRecommendationsForUser(UUID userId) {
        List<Product> recommendations = new ArrayList<>();

        // Проверяем статические правила
        for (RecommendationRuleSet rule : staticRules) {
            rule.check(userId).ifPresent(recommendations::add);
        }

        // Проверяем динамические правила
        List<RuleDto> dynamicRules = dynamicRuleService.getAllRules();
        for (RuleDto rule : dynamicRules) {
            dynamicRuleExecutor.checkRule(rule, userId)
                    .ifPresent(recommendations::add);
        }
        return recommendations;
    }
}
