package com.star.recommender.service;

import com.star.recommender.model.Client;
import com.star.recommender.repository.RecommendationsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientSearchService {

    private final RecommendationsRepository repository;

    public ClientSearchService(RecommendationsRepository repository) {
        this.repository = repository;
    }

    /**
     * Ищет пользователя по имени
     * Возвращает пользователя ТОЛЬКО если найден ровно один
     * Если 0 или больше 1 - возвращает null (пользователь не найден)
     */
    public Client findUniqueClientByName(String name) {
        // Разбиваем имя на части (предполагаем "Имя Фамилия")
        String[] parts = name.trim().split("\\s+");

        List<Client> clients;

        if (parts.length == 2) {
            // Если ввели "Имя Фамилия" - ищем точное совпадение
            clients = repository.findClientsByName(parts[0], parts[1]);
        } else {
            // Если ввели одно слово - ищем везде (по имени и фамилии)
            clients = repository.findClientsByNameLike(name);
        }
        if (clients.size() == 1) {
            return clients.get(0);
        }

        return null;
    }
}
