package com.star.recommender.controller;

import com.star.recommender.dto.RuleDto;
import com.star.recommender.service.DynamicRuleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/rule")
public class RuleController {

    private final DynamicRuleService ruleService;

    public RuleController(DynamicRuleService ruleService) {
        this.ruleService = ruleService;
    }

    @PostMapping
    public ResponseEntity<RuleDto> createRule(@RequestBody RuleDto ruleDto) {
        RuleDto created = ruleService.createRule(ruleDto);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<Map<String, List<RuleDto>>> getAllRules() {
        List<RuleDto> rules = ruleService.getAllRules();
        Map<String, List<RuleDto>> response = new HashMap<>();
        response.put("data", rules);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteRule(@PathVariable UUID productId) {
        ruleService.deleteRule(productId);
        return ResponseEntity.noContent().build();
    }
}
