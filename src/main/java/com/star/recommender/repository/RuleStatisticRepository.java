package com.star.recommender.repository;

import com.star.recommender.model.RuleStatistic;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RuleStatisticRepository extends JpaRepository<RuleStatistic, UUID> {

    @Modifying
    @Transactional
    @Query("DELETE FROM RuleStatistic rs WHERE rs.ruleId = :ruleId")
    void deleteByRuleId(@Param("ruleId") UUID ruleId);

    Optional<RuleStatistic> findByRuleId(UUID ruleId);
}
