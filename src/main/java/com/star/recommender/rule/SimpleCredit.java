package com.star.recommender.rule;

import com.star.recommender.model.Product;
import com.star.recommender.repository.RecommendationsRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Component
public class SimpleCredit implements RecommendationRuleSet {

    private static final UUID PRODUCT_ID = UUID.fromString("ab138afb-f3ba-4a93-b74f-0fcee86d447f");
    private static final String PRODUCT_NAME = "Простой кредит";
    private static final String PRODUCT_TEXT = "Откройте мир выгодных кредитов с нами! "
            + "Ищете способ быстро и без лишних хлопот получить нужную сумму? Тогда наш выгодный "
            + "кредит — именно то, что вам нужно! Мы предлагаем низкие процентные ставки, "
            + "гибкие условия и индивидуальный подход к каждому клиенту. Почему выбирают нас: "
            + "Быстрое рассмотрение заявки. Мы ценим ваше время, поэтому процесс рассмотрения заявки "
            + "занимает всего несколько часов. Удобное оформление. Подать заявку на кредит можно онлайн "
            + "на нашем сайте или в мобильном приложении. Широкий выбор кредитных продуктов. "
            + "Мы предлагаем кредиты на различные цели: покупку недвижимости, автомобиля, "
            + "образование, лечение и многое другое. Не упустите возможность воспользоваться выгодными "
            + "условиями кредитования от нашей компании!";

    private final RecommendationsRepository repository;

    public SimpleCredit(RecommendationsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Product> check(UUID userId) {
        // Правило 1: нет CREDIT продуктов
        boolean hasNoCredit = repository.hasNoProductOfType(userId, "CREDIT");
        if (!hasNoCredit) return Optional.empty();

        // Правило 2: пополнений DEBIT > трат DEBIT
        boolean depositsGreaterThanWithdrawals = repository.isDebitDepositsGreaterThanWithdrawals(userId);
        if (!depositsGreaterThanWithdrawals) return Optional.empty();

        // Правило 3: трат DEBIT > 100000
        BigDecimal debitWithdrawals = repository.getTotalWithdrawalsByProductType(userId, "DEBIT");
        if (debitWithdrawals.compareTo(new BigDecimal("100000")) <= 0) return Optional.empty();

        return Optional.of(new Product(PRODUCT_ID, PRODUCT_NAME, PRODUCT_TEXT));
    }

    @Override
    public UUID getProductId() {
        return PRODUCT_ID;
    }
}
