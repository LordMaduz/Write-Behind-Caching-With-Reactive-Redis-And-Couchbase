package com.ruchira.component;

import com.ruchira.model.Product;
import com.ruchira.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.AsyncIterator;
import org.redisson.api.map.MapLoaderAsync;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletionStage;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductLoader implements MapLoaderAsync<String, Product> {

    private final ProductRepository productRepository;

    @Override
    public CompletionStage<Product> load(String key) {
        return productRepository.findById(key).toFuture();

    }

    @Override
    public AsyncIterator<String> loadAllKeys() {
        return null;
    }

}
