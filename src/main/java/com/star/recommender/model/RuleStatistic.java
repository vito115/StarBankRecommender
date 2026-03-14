package com.star.recommender.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "rule_statistics")
public class RuleStatistic {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "rule_id", nullable = false, unique = true)
    private UUID ruleId;

    @Column(name = "trigger_count", nullable = false)
    private long triggerCount = 0;

    public RuleStatistic() {
    }

    public RuleStatistic(UUID ruleId) {
        this.ruleId = ruleId;
        this.triggerCount = 0;
    }

    public UUID getRuleId() {
        return ruleId;
    }

    public void setRuleId(UUID ruleId) {
        this.ruleId = ruleId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public long getTriggerCount() {
        return triggerCount;
    }

    public void setTriggerCount(long triggerCount) {
        this.triggerCount = triggerCount;
    }

    public void incrementCount() {
        this.triggerCount++;
    }
}
