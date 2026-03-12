package com.star.recommender.controller;

import com.star.recommender.cache.QueryCacheManager;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/management")
public class ManagementController {

    private final QueryCacheManager cacheManager;
    private final BuildProperties buildProperties;

    public ManagementController(QueryCacheManager cacheManager,
                                BuildProperties buildProperties) {
        this.cacheManager = cacheManager;
        this.buildProperties = buildProperties;
    }

    @PostMapping("/clear-caches")
    public ResponseEntity<Void> clearCaches() {
        cacheManager.clearAll();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, String>> getInfo() {
        Map<String, String> info = new HashMap<>();
        info.put("name", buildProperties.getName());
        info.put("version", buildProperties.getVersion());
        return ResponseEntity.ok(info);
    }
}
