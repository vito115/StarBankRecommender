package com.star.recommender.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class RuleStatisticDto {

    @JsonProperty("rule_id")
    private UUID ruleId;

    @JsonProperty("count")
    private long count;

    public RuleStatisticDto() {
    }

    public RuleStatisticDto(UUID ruleId, long count) {
        this.ruleId = ruleId;
        this.count = count;
    }

    public UUID getRuleId() {
        return ruleId;
    }

    public void setRuleId(UUID ruleId) {
        this.ruleId = ruleId;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
