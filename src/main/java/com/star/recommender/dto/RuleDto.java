package com.star.recommender.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

/**
 * DTO для передачи данных правила через REST API
 */
public class RuleDto {
    private UUID id;

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("product_id")
    private UUID productId;

    @JsonProperty("product_text")
    private String productText;

    @JsonProperty("rule")
    private List<QueryDto> rule;

    public RuleDto() {
    }

    public RuleDto(UUID id, String productName, UUID productId, String productText, List<QueryDto> rule) {
        this.id = id;
        this.productName = productName;
        this.productId = productId;
        this.productText = productText;
        this.rule = rule;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public String getProductText() {
        return productText;
    }

    public void setProductText(String productText) {
        this.productText = productText;
    }

    public List<QueryDto> getRule() {
        return rule;
    }

    public void setRule(List<QueryDto> rule) {
        this.rule = rule;
    }

    /**
     * Внутренний класс для одного запроса в правиле
     */

    public static class QueryDto {
        private String query;               // тип запроса (USER_OF, и т.д.)
        private List<String> arguments;     // аргументы запроса
        private boolean negate;             // отрицание

        public QueryDto() {
        }

        public QueryDto(String query, List<String> arguments, boolean negate) {
            this.query = query;
            this.arguments = arguments;
            this.negate = negate;
        }

        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query;
        }

        public List<String> getArguments() {
            return arguments;
        }

        public void setArguments(List<String> arguments) {
            this.arguments = arguments;
        }

        public boolean isNegate() {
            return negate;
        }

        public void setNegate(boolean negate) {
            this.negate = negate;
        }
    }
}

/**
 * DTO для ответа со списком всех правил
 * Формат: { "data": [...] }
 */
class RuleListResponse {
    private List<RuleDto> data;

    public RuleListResponse() {
    }

    public RuleListResponse(List<RuleDto> data) {
        this.data = data;
    }

    public List<RuleDto> getData() {
        return data;
    }

    public void setData(List<RuleDto> data) {
        this.data = data;
    }
}
