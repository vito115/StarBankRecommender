# StarBank Recommender

Сервис персонализированных рекомендаций банковских продуктов для банка "Стар".

## 📋 О проекте

Проект представляет собой REST API и Telegram-бота, которые на основе анализа транзакций клиента предлагают ему подходящие банковские продукты. Система поддерживает как статические правила (из первого ТЗ), так и динамические правила, которые можно создавать через API.

## 🛠 Стек технологий

- **Java 17**
- **Spring Boot 4.0.2**
- **Spring MVC / REST API**
- **Spring Data JPA / JDBC**
- **H2 Database** (база знаний о клиентах, read-only)
- **PostgreSQL** (динамические правила и статистика)
- **Liquibase** (миграции БД)
- **Caffeine** (кэширование)
- **Telegram Bot API** (pengrad)

## 📚 Документация

- [Вики проекта](https://github.com/ваш-аккаунт/StarBankRecommender/wiki)
- [Требования](https://github.com/ваш-аккаунт/StarBankRecommender/wiki/requirements)
- [Архитектура](https://github.com/ваш-аккаунт/StarBankRecommender/wiki/architecture)
- [REST API](https://github.com/ваш-аккаунт/StarBankRecommender/wiki/api)
- [Развертывание](https://github.com/ваш-аккаунт/StarBankRecommender/wiki/deployment)

## 🚀 Быстрый старт

### Требования
- Java 17 или выше
- PostgreSQL 14 или выше
- Maven (или использовать ./mvnw)

### Сборка проекта
```bash
./mvnw clean package