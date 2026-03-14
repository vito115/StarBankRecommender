package com.star.recommender.bot;

import com.star.recommender.model.Client;
import com.star.recommender.model.Product;
import com.star.recommender.service.ClientSearchService;
import com.star.recommender.service.RecommendationService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.UUID;

@Component
public class StarBankBot extends TelegramLongPollingBot {

    private static final String BOT_TOKEN = "8794188702:AAFRVorXCLfCDP1QU5frVzzluwSf6f4Sd7s";
    private static final String BOT_USERNAME = "StarBankRecommenderBot";

    private final ClientSearchService clientSearchService;
    private final RecommendationService recommendationService;

    public StarBankBot(ClientSearchService clientSearchService,
                       RecommendationService recommendationService) {
        this.clientSearchService = clientSearchService;
        this.recommendationService = recommendationService;
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }

        String messageText = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();

        if (messageText.equals("/start")) {
            sendMessage(chatId, "Привет! Используй команду /recommend Имя Фамилия");
        } else if (messageText.startsWith("/recommend")) {
            handleRecommendCommand(chatId, messageText);
        } else {
            sendMessage(chatId, "Неизвестная команда. Введите /start для справки.");
        }
    }

    private void handleRecommendCommand(long chatId, String messageText) {
        String name = messageText.replace("/recommend", "").trim();

        if (name.isEmpty()) {
            sendMessage(chatId, "Пожалуйста, укажите имя после команды. " +
                    "\nНапример: /recommend Иван Петров");
            return;
        }

        Client client = clientSearchService.findUniqueClientByName(name);

        if (client == null) {
            sendMessage(chatId, "Пользователь не найден");
            return;
        }

        UUID userId = UUID.fromString(client.getId());
        List<Product> products = recommendationService.getRecommendationsForUser(userId);

        StringBuilder answer = new StringBuilder();
        answer.append("Здравствуйте ").append(client.getFullName()).append("\n\n");
        answer.append("Новые продукты для вас:\n");

        for (Product p : products) {
            answer.append("• ").append(p.getName()).append("\n");
            answer.append("  ").append(p.getText()).append("\n\n");
        }
        sendMessage(chatId, answer.toString());
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
