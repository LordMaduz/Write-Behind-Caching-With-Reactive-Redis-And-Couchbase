package com.ruchira;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.couchbase.repository.auditing.EnableReactiveCouchbaseAuditing;
import org.springframework.data.redis.listener.ReactiveRedisMessageListenerContainer;

@SpringBootApplication
@EnableReactiveCouchbaseAuditing
@OpenAPIDefinition(info = @Info(title = "Redis Caching Application", version = "1.0", description = "Redis Caching Application"))
public class RedisWriteBehindCachingApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedisWriteBehindCachingApplication.class, args);
	}

}
