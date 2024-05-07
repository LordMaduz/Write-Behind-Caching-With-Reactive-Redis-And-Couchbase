package com.ruchira.component;

import com.ruchira.model.Product;
import com.ruchira.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.map.MapWriterAsync;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletionStage;


@Component
@RequiredArgsConstructor
@Slf4j
public class ProductWriter implements MapWriterAsync<String, Product> {

    private final ProductRepository productRepository;

    @Override
    public CompletionStage<Void> write(Map<String, Product> map) {
        return productRepository.saveAll(map.values()).collectList().then().toFuture();
    }

    @Override
    public CompletionStage<Void> delete(Collection<String> keys) {
        return Flux.fromIterable(keys).flatMap(productRepository::deleteById).collectList().then().toFuture();
    }
}
