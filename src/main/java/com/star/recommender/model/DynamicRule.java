package com.star.recommender.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * JPA сущность для хранения динамического правила
 * Соответствует таблице dynamic_rules в PostgreSQL
 */

@Entity
@Table(name = "dynamic_rules")
public class DynamicRule {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_id", nullable = false, unique = true)
    private UUID productId;

    @Column(name = "product_text", length = 2000)
    private String productText;

    @OneToMany(mappedBy = "rule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RuleQuery> queries = new ArrayList<>();

    public DynamicRule() {}

    public DynamicRule(String productName, UUID productId, String productText) {
        this.productName = productName;
        this.productId = productId;
        this.productText = productText;
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

    public List<RuleQuery> getQueries() {
        return queries;
    }

    public void setQueries(List<RuleQuery> queries) {
        this.queries = queries;
    }

    public void addQuery(RuleQuery query) {
        this.queries.add(query);
    }
}
