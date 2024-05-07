package com.ruchira.config;

import com.ruchira.model.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.stream.StreamReceiver;

import java.time.Duration;


@Configuration
public class RedisConfig {

    @Value("${redis.host}")
    private String host;

    @Value("${redis.port}")
    private int port;

    @Bean
    @Primary
    public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    public ReactiveRedisOperations<String, Object> reactiveRedisOperations(ReactiveRedisConnectionFactory reactiveRedisConnectionFactory) {
        return new ReactiveRedisTemplate<>(reactiveRedisConnectionFactory, redisSerializationContext());
    }

    private RedisSerializationContext<String, Object>  redisSerializationContext(){
        final Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        final RedisSerializationContext.RedisSerializationContextBuilder<String, Object> builder =
                RedisSerializationContext.newSerializationContext(new StringRedisSerializer());

        return builder.value(serializer).hashValue(serializer)
                .hashKey(serializer).build();
    }


    private StreamReceiver.StreamReceiverOptions<String, MapRecord<String, String, String>> streamReceiverOptions(){
        return StreamReceiver.StreamReceiverOptions.builder().serializer(redisSerializationContext()).pollTimeout(Duration.ofMillis(100))
                .build();
    }

    @Bean
    public StreamReceiver<String, MapRecord<String, String, String>> streamReceiver(final ReactiveRedisConnectionFactory reactiveRedisConnectionFactory){
        return StreamReceiver.create(reactiveRedisConnectionFactory, streamReceiverOptions());
    }

}
