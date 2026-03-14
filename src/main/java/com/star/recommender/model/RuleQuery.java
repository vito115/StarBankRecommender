package com.star.recommender.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "rule_queries")
public class RuleQuery {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "rule_id", nullable = false)
    private DynamicRule rule;

    @Column(nullable = false)
    private String query; // тип запроса: USER_OF, ACTIVE_USER_OF и т.д.

    // Аргументы хранятся в отдельной таблице (один ко многим)
    @ElementCollection
    @CollectionTable(name = "query_arguments", joinColumns = @JoinColumn(name = "query_id"))
    @Column(name = "argument")
    @OrderColumn(name = "argument_order") // сохраняем порядок аргументов
    private List<String> arguments = new ArrayList<>();

    @Column(nullable = false)
    private boolean negate; // отрицание true/false

    public RuleQuery() {
    }

    public RuleQuery(DynamicRule rule, String query, List<String> arguments, boolean negate) {
        this.rule = rule;
        this.query = query;
        this.arguments = arguments;
        this.negate = negate;
    }

    public DynamicRule getRule() {
        return rule;
    }

    public void setRule(DynamicRule rule) {
        this.rule = rule;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
