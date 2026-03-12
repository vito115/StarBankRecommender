package com.star.recommender.dto;

import java.util.List;

public class RuleStatsResponse {

    private List<RuleStatisticDto> stats;

    public RuleStatsResponse() {}

    public RuleStatsResponse(List<RuleStatisticDto> stats) {
        this.stats = stats;
    }

    public List<RuleStatisticDto> getStats() {
        return stats;
    }

    public void setStats(List<RuleStatisticDto> stats) {
        this.stats = stats;
    }
}
