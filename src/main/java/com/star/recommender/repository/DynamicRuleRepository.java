package com.star.recommender.repository;

import com.star.recommender.model.DynamicRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * JPA репозиторий для работы с динамическими правилами
 * Spring Data JPA автоматически реализует все базовые методы
 */

@Repository
public interface DynamicRuleRepository extends JpaRepository<DynamicRule, UUID> {

    /**
     * Проверяет, существует ли правило для данного продукта
     * Spring Data JPA сам реализует этот метод по имени
     */
    boolean existsByProductId(UUID productId);
}
