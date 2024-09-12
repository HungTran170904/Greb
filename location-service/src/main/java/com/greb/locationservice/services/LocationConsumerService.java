package com.greb.locationservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greb.locationservice.dtos.LocationConverter;
import com.greb.locationservice.dtos.LocationDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationConsumerService {
    private final MongoTemplate mongoTemplate;
    private final LocationConverter locationConverter;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @RetryableTopic(attempts = "4")
    @SneakyThrows
    @KafkaListener(topics="output-location-topic")
    public void consumeLocationMessages(
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Payload  String jsonStr){
        var dto = objectMapper.readValue(jsonStr, LocationDto.class);
        var location= locationConverter.fromDto(key, dto);
        mongoTemplate.save(location);
    }

    @DltHandler
    public void listenDtl(
            String payload,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.OFFSET) long offset){
        log.info("New error message, topic: {}, Payload:{}, offset:{}", topic, payload, offset);
    }
}
