package com.star.recommender.rule;

import com.star.recommender.model.Product;
import com.star.recommender.repository.RecommendationsRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Component
public class Invest500Rule implements RecommendationRuleSet{

    private static final UUID PRODUCT_ID = UUID.fromString("147f6a0f-3b91-413b-ab99-87f081d60d5a");
    private static final String PRODUCT_NAME = "Invest 500";
    private static final String PRODUCT_TEXT =
            "Откройте свой путь к успеху с индивидуальным инвестиционным счетом (ИИС) " +
            "от нашего банка! Воспользуйтесь налоговыми льготами и начните инвестировать " +
            "с умом. Пополните счет до конца года и получите выгоду в виде вычета на взнос " +
            "в следующем налоговом периоде. Не упустите возможность разнообразить свой " +
            "портфель, снизить риски и следить за актуальными рыночными тенденциями. " +
            "Откройте ИИС сегодня и станьте ближе к финансовой независимости!";

    private final RecommendationsRepository repository;

    public Invest500Rule(RecommendationsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Product> check(UUID userId) {
        // Правило 1: есть DEBIT продукт
        boolean hasDebit = repository.hasAnyProductOfType(userId, "DEBIT");
        if (!hasDebit) return Optional.empty();

        // Правило 2: нет INVEST продуктов
        boolean hasNoInvest = repository.hasNoProductOfType(userId, "INVEST");
        if (!hasNoInvest) return Optional.empty();

        // Правило 3: пополнений SAVING > 1000
        BigDecimal savingDeposits = repository.getTotalSavingDeposits(userId);
        if (savingDeposits.compareTo(new BigDecimal("1000")) <= 0) return Optional.empty();

        return Optional.of(new Product(PRODUCT_ID, PRODUCT_NAME, PRODUCT_TEXT));
    }

    @Override
    public UUID getProductId() {
        return PRODUCT_ID;
    }
}
