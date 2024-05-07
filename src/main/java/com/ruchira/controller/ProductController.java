package com.ruchira.controller;

import com.ruchira.model.Product;
import com.ruchira.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.nio.charset.Charset;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public Mono<Product> createNewProduct(@RequestBody final Product product) {
        return productService.createNewProduct(product);
    }

    @PutMapping
    public Mono<Product> updateProduct(@RequestBody final Product product) {
        return productService.updateProduct(product);
    }

    @GetMapping
    public Flux<Product> getAllProducts() {
        return productService.getProducts().log();
    }

    @GetMapping(path = "/{id}/{fromCache}")
    public Mono<Product> getAllProducts(@PathVariable final String id, @PathVariable final Boolean fromCache) {
        return Mono.fromCallable(() -> {
                    log.info("Computing Basic Operations Inside GetAll Products Methods");
                    return "SUCCESS";
                })
                .log()
                .flatMap(result -> productService.findById(id, fromCache))
                .log()
                .map(product -> {
                    log.info("Fetched Product from DB");
                    return product;
                }).log();
    }

    @GetMapping("/read-file")
    public Mono<String> readFile() throws IOException {
        return readFileAsAString().flatMap(result-> Mono.just("SUCCESFULL")).log();
    }

    private Mono<Boolean> readFileAsAString() throws IOException {
        return Mono.fromCallable(()-> {
            log.info("Entering the Method Read File As a String");
            final String msg = StreamUtils.copyToString(new ClassPathResource("application-dev.yml").getInputStream(), Charset.defaultCharset());
            log.info("The Content Found in the File: {}", msg);

            return true;
        }).subscribeOn(Schedulers.boundedElastic()).publishOn(Schedulers.parallel());
    }

}
