package com.star.recommender.controller;

import com.star.recommender.dto.RuleStatsResponse;
import com.star.recommender.service.RuleStatisticService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rule")
public class RuleStatsController {

    private final RuleStatisticService statisticService;

    public RuleStatsController(RuleStatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @GetMapping("/stats")
    public ResponseEntity<RuleStatsResponse> getStats() {
        var stats = statisticService.getAllStatistics();
        return ResponseEntity.ok(new RuleStatsResponse(stats));
    }
}
