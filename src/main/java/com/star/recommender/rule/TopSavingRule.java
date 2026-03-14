package com.star.recommender.rule;

import com.star.recommender.model.Product;
import com.star.recommender.repository.RecommendationsRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Component
public class TopSavingRule implements RecommendationRuleSet {

    private static final UUID PRODUCT_ID = UUID.fromString("59efc529-2fff-41af-baff-90ccd7402925");
    private static final String PRODUCT_NAME = "Top Saving";
    private static final String PRODUCT_TEXT = "Откройте свою собственную «Копилку» с нашим банком!"
            + "«Копилка» — это уникальный банковский инструмент, который поможет вам легко и удобно накапливать"
            + "деньги на важные цели. Больше никаких забытых чеков и потерянных квитанций — всё под контролем!"
            + "Преимущества «Копилки»: Накопление средств на конкретные цели. Установите лимит и срок "
            + "накопления, и банк будет автоматически переводить определенную сумму на ваш счет. "
            + "Прозрачность и контроль. Отслеживайте свои доходы и расходы, контролируйте процесс "
            + "накопления и корректируйте стратегию при необходимости. Безопасность и надежность. "
            + "Ваши средства находятся под защитой банка, а доступ к ним возможен только через мобильное "
            + "приложение или интернет-банкинг. "
            + "Начните использовать «Копилку» уже сегодня и станьте ближе к своим финансовым целям!";

    private final RecommendationsRepository repository;

    public TopSavingRule(RecommendationsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Product> check(UUID userId) {
        // Правило 1: есть DEBIT продукт
        boolean hasDebit = repository.hasAnyProductOfType(userId, "DEBIT");
        if (!hasDebit) return Optional.empty();

        // Правило 2: пополнений DEBIT >= 50000 ИЛИ пополнений SAVING >= 50000
        BigDecimal debitDeposits = repository.getTotalDepositsByProductType(userId, "DEBIT");
        BigDecimal savingDeposits = repository.getTotalDepositsByProductType(userId, "SAVING");
        boolean enoughDeposits = debitDeposits.compareTo(new BigDecimal("50000")) >= 0
                || savingDeposits.compareTo(new BigDecimal("50000")) >= 0;

        if (!enoughDeposits) return Optional.empty();

        // Правило 3: пополнений DEBIT > трат DEBIT
        boolean depositsGreaterThanWithdrawals = repository.isDebitDepositsGreaterThanWithdrawals(userId);
        if (!depositsGreaterThanWithdrawals) return Optional.empty();

        return Optional.of(new Product(PRODUCT_ID, PRODUCT_NAME, PRODUCT_TEXT));
    }

    @Override
    public UUID getProductId() {
        return PRODUCT_ID;
    }
}
