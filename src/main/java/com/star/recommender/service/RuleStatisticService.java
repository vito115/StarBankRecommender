package com.star.recommender.service;

import com.star.recommender.dto.RuleStatisticDto;
import com.star.recommender.model.DynamicRule;
import com.star.recommender.model.RuleStatistic;
import com.star.recommender.repository.DynamicRuleRepository;
import com.star.recommender.repository.RuleStatisticRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RuleStatisticService {

    private final RuleStatisticRepository statisticRepository;
    private final DynamicRuleRepository ruleRepository;

    public RuleStatisticService(RuleStatisticRepository statisticRepository,
                                DynamicRuleRepository ruleRepository) {
        this.statisticRepository = statisticRepository;
        this.ruleRepository = ruleRepository;
    }

    @Transactional
    public void incrementStatistic(UUID ruleId) {
        RuleStatistic statistic = statisticRepository.findByRuleId(ruleId)
                .orElseGet(() -> {
                    RuleStatistic newStat = new RuleStatistic(ruleId);
                    return statisticRepository.save(newStat);
                });
        statistic.incrementCount();
        statisticRepository.save(statistic);
    }

    @Transactional(readOnly = true)
    public List<RuleStatisticDto> getAllStatistics() {
        // 1. Получаем все статистики из БД
        List<RuleStatistic> stats = statisticRepository.findAll();

        // 2. Получаем все правила
        List<DynamicRule> allRules = ruleRepository.findAll();

        // 3. Создаем список для результатов
        List<RuleStatisticDto> result = new ArrayList<>();

        // 4. Добавляем существующие статистики
        for (RuleStatistic stat : stats) {
            result.add(new RuleStatisticDto(stat.getRuleId(), stat.getTriggerCount()));
        }

        // 5. Добавляем правила без статистики (count = 0)
        for (DynamicRule rule : allRules) {
            boolean hasStat = stats.stream()
                    .anyMatch(s -> s.getRuleId().equals(rule.getId()));
            if (!hasStat) {
                result.add(new RuleStatisticDto(rule.getId(), 0));
            }
        }

        return result;
    }
}
