package com.star.recommender.service;

import com.star.recommender.dto.RuleDto;
import com.star.recommender.model.DynamicRule;
import com.star.recommender.model.RuleQuery;
import com.star.recommender.model.RuleStatistic;
import com.star.recommender.repository.DynamicRuleRepository;
import com.star.recommender.repository.RuleStatisticRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Сервис для работы с динамическими правилами
 * Содержит бизнес-логику создания, чтения и удаления правил
 */

@Service
public class DynamicRuleService {

    private final DynamicRuleRepository ruleRepository;
    private final RuleStatisticRepository statisticRepository;

    public DynamicRuleService(DynamicRuleRepository ruleRepository,
                              RuleStatisticRepository statisticRepository) {
        this.ruleRepository = ruleRepository;
        this.statisticRepository = statisticRepository;
    }

    /**
     * Создание нового правила
     *
     * @Transactional - все операции в одной транзакции
     */
    @Transactional
    public RuleDto createRule(RuleDto ruleDto) {

        // 1. Создаем сущность DynamicRule из DTO
        DynamicRule rule = new DynamicRule(
                ruleDto.getProductName(),
                ruleDto.getProductId(),
                ruleDto.getProductText()
        );

        // 2. Добавляем все запросы из правила
        ruleDto.getRule().forEach(queryDto -> {
            RuleQuery query = new RuleQuery(
                    rule,
                    queryDto.getQuery(),
                    queryDto.getArguments(),
                    queryDto.isNegate()
            );
            rule.getQueries().add(query);
        });

        // 3. Сохраняем в базу данных
        DynamicRule saved = ruleRepository.save(rule);

        // 4. Создаем статистику только с ID (без связи)
        RuleStatistic statistic = new RuleStatistic(saved.getId());
        statisticRepository.save(statistic);

        // 5. Преобразуем обратно в DTO и возвращаем
        return convertToDto(saved);
    }

    @Transactional(readOnly = true)
    public List<RuleDto> getAllRules() {
        return ruleRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteRule(UUID productId) {
        statisticRepository.deleteByRuleId(productId);
        ruleRepository.deleteById(productId);
    }

    private RuleDto convertToDto(DynamicRule rule) {
        List<RuleDto.QueryDto> queryDtos = rule.getQueries().stream()
                .map(q -> new RuleDto.QueryDto(
                        q.getQuery(),
                        q.getArguments(),
                        q.isNegate()
                ))
                .collect(Collectors.toList());

        return new RuleDto(
                rule.getId(),
                rule.getProductName(),
                rule.getProductId(),
                rule.getProductText(),
                queryDtos
        );
    }
}
