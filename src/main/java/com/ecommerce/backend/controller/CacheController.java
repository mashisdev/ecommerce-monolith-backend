package com.ecommerce.backend.controller;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@PreAuthorize("hasAuthority('ADMIN')")
@RequiredArgsConstructor
@RequestMapping("admin/cache")
public class CacheController {

    private final CacheManager cacheManager;

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getCacheStats() {
        Map<String, Object> statsMap = new HashMap<>();

        cacheManager.getCacheNames().forEach(name -> {
            CaffeineCache caffeineCache = (CaffeineCache) cacheManager.getCache(name);
            if (caffeineCache != null) {
                Cache<Object, Object> nativeCache = caffeineCache.getNativeCache();
                CacheStats stats = nativeCache.stats();

                Map<String, Object> details = new HashMap<>();
                details.put("entries", nativeCache.estimatedSize());
                details.put("stats", stats.toString());

                statsMap.put(name, details);
            }
        });

        return ResponseEntity.ok(statsMap);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> clearCacheByName(@PathVariable String name) {
        org.springframework.cache.Cache cache = cacheManager.getCache(name);
        if (cache != null) {
            cache.clear();
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> clearAllCaches() {
        cacheManager.getCacheNames().forEach(name -> {
            org.springframework.cache.Cache cache = cacheManager.getCache(name);
            if (cache != null) {
                cache.clear();
            }
        });
        return ResponseEntity.noContent().build();
    }
}