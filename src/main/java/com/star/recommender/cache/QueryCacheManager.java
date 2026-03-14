package com.star.recommender.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Менеджер кэша для результатов запросов к H2 базе
 * Так как данные не меняются, мы можем кэшировать результаты
 */
@Component
public class QueryCacheManager {

    private static final Logger log = LoggerFactory.getLogger(QueryCacheManager.class);
    // Кэш для результатов hasAnyProductOfType
    private final Cache<String, Boolean> hasProductCache = Caffeine.newBuilder()
            .maximumSize(1000)          // храним до 1000 результатов
            .expireAfterWrite(10, TimeUnit.MINUTES) // живут 10 мин
            .build();

    // Кэш для сумм пополнений (DEPOSIT)
    private final Cache<String, BigDecimal> depositSumCache = Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();

    // Кэш для сумм трат (WITHDRAW)
    private final Cache<String, BigDecimal> withdrawSumCache = Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();

    private String buildKey(UUID userId, String... params) {
        StringBuilder key = new StringBuilder(userId.toString());
        for (String param : params) {
            key.append(":").append(param);
        }
        return key.toString();
    }

    // Методы для кэша hasProduct:

    public Boolean getHasProduct(UUID userId, String productType) {
        return hasProductCache.getIfPresent(buildKey(userId, productType));
    }

    public void putHasProduct(UUID userId, String productType, Boolean value) {
        hasProductCache.put(buildKey(userId, productType), value);
    }

    // Методы для кэша depositSum:

    public BigDecimal getDepositSum(UUID userId, String productType) {
        return depositSumCache.getIfPresent(buildKey(userId, productType));
    }

    public void putDepositSum(UUID userId, String productType, BigDecimal value) {
        depositSumCache.put(buildKey(userId, productType), value);
    }

    // Методы для кэша withdrawSum:

    public BigDecimal getWithdrawSum(UUID userId, String productType) {
        return withdrawSumCache.getIfPresent(buildKey(userId, productType));
    }

    public void putWithdrawSum(UUID userId, String productType, BigDecimal value) {
        withdrawSumCache.put(buildKey(userId, productType), value);
    }

    // Очистка всех кэшей
    public void clearAll() {
        hasProductCache.invalidateAll();
        depositSumCache.invalidateAll();
        withdrawSumCache.invalidateAll();
        log.info("All caches cleared");
    }
}
