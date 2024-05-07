package com.ruchira.repo;

import com.ruchira.model.Product;
import org.springframework.data.couchbase.repository.ReactiveCouchbaseRepository;
import org.springframework.data.couchbase.repository.auditing.EnableReactiveCouchbaseAuditing;
import org.springframework.data.couchbase.repository.config.EnableReactiveCouchbaseRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableReactiveCouchbaseRepositories
public interface ProductRepository extends ReactiveCouchbaseRepository<Product, String> {
}
