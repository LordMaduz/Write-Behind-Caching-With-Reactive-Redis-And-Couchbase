package com.ruchira.service;

import com.ruchira.model.Product;
import com.ruchira.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.Charset;

@Component
@RequiredArgsConstructor
public class ProductService {

    private final ProductCache productCache;
    private final ProductRepository productRepository;

    public Mono<Product> createNewProduct(final Product product) {
        return productRepository.save(product).flatMap(savedProduct ->
                productCache.updateProduct(savedProduct).map(result -> savedProduct)
        );
    }

    public Mono<Product> updateProduct(final Product product) {
        return productCache.updateProduct(product);
    }

    public Flux<Product> getProducts() {
        return productRepository.findAll();
    }

    public Mono<Product> findById(final String id, final boolean isFromCache) {
        if (isFromCache) {
            productCache.getById(id);
        }
        return productRepository.findById(id);
    }


}
