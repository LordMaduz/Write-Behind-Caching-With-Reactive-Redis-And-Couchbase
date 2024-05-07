package com.ruchira.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.stream.StreamReceiver;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;

@Component
@RequiredArgsConstructor
@Slf4j
public class DefaultEventReceiverService {

    private final StreamReceiver<String, MapRecord<String, String, String>> receiver;


    @PostConstruct
    public Disposable readStream() {
        return receiver.receive(StreamOffset.fromStart("my-stream")).subscribe(record -> {
            log.info("Record Received: {}", record);
        });
    }

}

