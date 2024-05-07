package com.ruchira.service;

import com.ruchira.component.ProductLoader;
import com.ruchira.component.ProductWriter;
import com.ruchira.model.Product;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.redisson.api.MapOptions;
import org.redisson.api.RMapReactive;
import org.redisson.api.RedissonReactiveClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ProductCache {

    private static final String PRODUCT_REDIS_KEY = "PRODUCT:REDIS";
    private final RedissonReactiveClient redissonClient;
    private final ProductLoader productLoader;
    private final ProductWriter productWriter;
    private MapOptions<String, Product> cacheWriteOptions;
    private MapOptions<String, Product> cacheReadOptions;

    @PostConstruct
    private void init() {
        cacheReadOptions = MapOptions.<String, Product>defaults().loaderAsync(productLoader);
        cacheWriteOptions = MapOptions.<String, Product>defaults()
                .writerAsync(productWriter)
                .writeBehindDelay(10 * 1000)
                .writeBehindBatchSize(10)
                .writeMode(MapOptions.WriteMode.WRITE_BEHIND);
    }

    public Mono<Product> updateProduct(final Product product) {
        return getRedisMap(cacheWriteOptions).put(product.getId(), product);
    }

    private RMapReactive<String, Product> getRedisMap() {
        return redissonClient.getMap(toRedisKey());
    }

    private RMapReactive<String, Product> getRedisMap(MapOptions<String, Product> options) {
        return redissonClient.getMap(toRedisKey(), options);
    }

    public Mono<Product> getById(String id) {
        return getRedisMap(cacheReadOptions).get(id);
    }


    private String toRedisKey() {
        return PRODUCT_REDIS_KEY;
    }

    public Mono<Void> clearMap() {
        return getRedisMap().expire(Instant.now()).then();
    }

}
