package com.star.recommender.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
public class RecommendationsRepository {

    private final JdbcTemplate jdbcTemplate;

    public RecommendationsRepository(
            @Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Проверяет, использует ли пользователь продукты указанного типа
    public boolean hasAnyProductOfType(UUID userId, String productType) {
        String sql = """
                SELECT COUNT(*) > 0
                FROM operations o
                JOIN products p ON o.product_id = p.id
                WHERE o.user_id = ? AND p.type = ?
                """;

        return Boolean.TRUE.equals(
                jdbcTemplate.queryForObject(sql, Boolean.class, userId.toString(),productType)
        );
    }

    // Проверяет, не использует ли пользователь продукты указанного типа
    public boolean hasNoProductOfType(UUID userId, String productType) {
        return !hasAnyProductOfType(userId, productType);
    }

    // Сумма пополнений (DEPOSIT) по типу продукта
    public BigDecimal getTotalDepositsByProductType(UUID userId, String productType) {
        String sql = """
                SELECT COALESCE (SUM(o.amount), 0)
                FROM operations o
                JOIN products p ON o.product_id = p.id
                WHERE o.user_id = ? AND p.type = ? AND o.type = 'DEPOSIT'
                """;

        return jdbcTemplate.queryForObject(sql, BigDecimal.class, userId.toString(), productType);
    }

    // Сумма трат (WITHDRAW) по типу продукта
    public BigDecimal getTotalWithdrawalsByProductType(UUID userId, String productType) {
        String sql = """
                SELECT COALESCE(SUM(o.amount), 0)
                FROM operations o
                JOIN products p ON o.product_id = p.id
                WHERE o.user_id = ? AND p.type = ? AND o.type = 'WITHDRAW'
                """;

        return jdbcTemplate.queryForObject(sql, BigDecimal.class, userId.toString(), productType);
    }

    // Сумма пополнений SAVING (специально для Invest 500)
    public BigDecimal getTotalSavingDeposits(UUID userId) {
        String sql = """
                SELECT COALESCE(SUM(o.amount), 0)
                FROM operations o
                JOIN products p ON o.product_id = p.id
                WHERE o.user_id = ? AND p.type = 'SAVING' AND o.type = 'DEPOSIT'
                """;

        return jdbcTemplate.queryForObject(sql, BigDecimal.class, userId.toString());
    }

    // Проверяет, что пополнений DEBIT больше чем трат DEBIT
    public boolean isDebitDepositsGreaterThanWithdrawals(UUID userId) {
        BigDecimal deposits = getTotalDepositsByProductType(userId, "DEBIT");
        BigDecimal withdrawals = getTotalWithdrawalsByProductType(userId, "DEBIT");
        return deposits.compareTo(withdrawals) > 0;
    }
}
