package com.star.recommender.rule;

import com.star.recommender.dto.RuleDto;
import com.star.recommender.model.Product;
import com.star.recommender.repository.RecommendationsRepository;
import com.star.recommender.service.RuleStatisticService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Исполнитель динамических правил
 * Проверяет, подходит ли пользователь под динамическое правило
 */
@Component
public class DynamicRuleExecutor {

    private final RecommendationsRepository repository;
    private final RuleStatisticService statisticService;

    public DynamicRuleExecutor(RecommendationsRepository repository,
                               RuleStatisticService statisticService) {
        this.repository = repository;
        this.statisticService = statisticService;
    }

    public Optional<Product> checkRule(RuleDto rule, UUID userId) {
        for (RuleDto.QueryDto query : rule.getRule()) {
            boolean result = executeQuery(query, userId);

            if (query.isNegate() ? result : !result) {
                return Optional.empty();
            }
        }

        // Правило сработало - увеличиваем счетчик!
        statisticService.incrementStatistic(rule.getId());

        Product product = new Product(
                rule.getProductId(),
                rule.getProductName(),
                rule.getProductText()
        );
        return Optional.of(product);
    }

    private boolean executeQuery(RuleDto.QueryDto query, UUID userId) {
        switch (query.getQuery()) {
            case "USER_OF":
                return checkUserOf(userId, query.getArguments().get(0));

            case "ACTIVE_USER_OF":
                return checkActiveUserOf(userId, query.getArguments().get(0));

            case "TRANSACTION_SUM_COMPARE":
                return checkSumCompare(userId, query.getArguments());

            case "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW":
                return checkDepositWithdrawCompare(userId, query.getArguments());

            default:
                return false;
        }
    }

    private boolean checkUserOf(UUID userId, String productType) {
        return repository.hasAnyProductOfType(userId, productType);
    }

    private boolean checkActiveUserOf(UUID userId, String productType) {
        return false;
    }

    private boolean checkSumCompare(UUID userId, List<String> args) {
        String productType = args.get(0);
        String transactionType = args.get(1);
        String operator = args.get(2);
        int threshold = Integer.parseInt(args.get(3));

        BigDecimal sum;
        if ("DEPOSIT".equals(transactionType)) {
            sum = repository.getTotalDepositsByProductType(userId, productType);
        } else {
            sum = repository.getTotalWithdrawalsByProductType(userId, productType);
        }

        return compare(sum, operator, new BigDecimal(threshold));
    }

    private boolean checkDepositWithdrawCompare(UUID userId, List<String> args) {
        String productType = args.get(0);
        String operator = args.get(1);

        BigDecimal deposits = repository.getTotalDepositsByProductType(userId, productType);
        BigDecimal withdrawals = repository.getTotalWithdrawalsByProductType(userId, productType);

        return compare(deposits, operator, withdrawals);
    }

    private boolean compare(BigDecimal left, String operator, BigDecimal right) {
        switch (operator) {
            case ">": return left.compareTo(right) > 0;
            case "<": return left.compareTo(right) < 0;
            case "=": return left.compareTo(right) == 0;
            case ">=": return left.compareTo(right) >= 0;
            case "<=": return left.compareTo(right) <= 0;
            default:
                return false;
        }
    }
}
