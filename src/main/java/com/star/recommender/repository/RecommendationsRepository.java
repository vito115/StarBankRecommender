package com.star.recommender.repository;

import com.star.recommender.cache.QueryCacheManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
public class RecommendationsRepository {

    private final JdbcTemplate jdbcTemplate;
    private final QueryCacheManager cacheManager;

    public RecommendationsRepository(
            @Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate,
            QueryCacheManager cacheManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.cacheManager = cacheManager;
    }

    private BigDecimal executeSumQuery(UUID userId, String productType, String transactionType) {
        String sql =String.format( """
                SELECT COALESCE(SUM(o.amount), 0)
                FROM transactions o
                JOIN products p ON o.product_id = p.id
                WHERE o.user_id = ? AND p.type = ? AND o.type = '%s'
                """, transactionType);

        return jdbcTemplate.queryForObject(
                sql, BigDecimal.class, userId.toString(), productType
        );
    }

    // Сумма пополнений (DEPOSIT) по типу продукта c кэшированием
    public BigDecimal getTotalDepositsByProductType(UUID userId, String productType) {

        // проверяем кэш
        BigDecimal cached = cacheManager.getDepositSum(userId, productType);
        if (cached != null) {
            return cached;
        }

        // выполняем запрос
        BigDecimal result = executeSumQuery(userId, productType, "DEPOSIT");

        // сохраняем в кэш
        cacheManager.putDepositSum(userId, productType, result);
        return result;
    }

    // Сумма трат (WITHDRAW) по типу продукта с кэшированием
    public BigDecimal getTotalWithdrawalsByProductType(UUID userId, String productType) {

        // проверяем кэш
        BigDecimal cached = cacheManager.getWithdrawSum(userId, productType);
        if (cached != null) {
            return cached;
        }

        // выполняем запрос
        BigDecimal result = executeSumQuery(userId, productType, "WITHDRAW");

        // сохраняем в кэш
        cacheManager.putWithdrawSum(userId, productType, result);
        return result;
    }

    // Проверяет, использует ли пользователь продукты указанного типа
    public boolean hasAnyProductOfType(UUID userId, String productType) {
        // Проверяем кэш
        Boolean cached = cacheManager.getHasProduct(userId, productType);
        if (cached != null) {
            return cached;
        }

        // Выполняем запрос
        String sql = """
                SELECT COUNT(*) > 0
                FROM transactions o
                JOIN products p ON o.product_id = p.id
                WHERE o.user_id = ? AND p.type = ?
                """;

        Boolean result = Boolean.TRUE.equals(
                jdbcTemplate.queryForObject(sql, Boolean.class, userId.toString(),
                        productType)
        );

        // Сохраняем в кэш
        cacheManager.putHasProduct(userId, productType, result);
        return result;
    }

    // Проверяет, не использует ли пользователь продукты указанного типа
    public boolean hasNoProductOfType(UUID userId, String productType) {
        return !hasAnyProductOfType(userId, productType);
    }

    // Сумма пополнений SAVING (специально для Invest 500)
    public BigDecimal getTotalSavingDeposits(UUID userId) {
        return getTotalDepositsByProductType(userId, "SAVING");
    }

    // Проверяет, что пополнений DEBIT больше чем трат DEBIT
    public boolean isDebitDepositsGreaterThanWithdrawals(UUID userId) {
        BigDecimal deposits = getTotalDepositsByProductType(userId, "DEBIT");
        BigDecimal withdrawals = getTotalWithdrawalsByProductType(userId, "DEBIT");
        return deposits.compareTo(withdrawals) > 0;
    }
}
