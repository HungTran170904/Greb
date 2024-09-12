package com.greb.kafkastreamservice.configs;

import com.greb.kafkastreamservice.constraints.KafkaTopics;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.errors.LogAndContinueExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@EnableKafkaStreams
public class KafkaConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean(name= KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    KafkaStreamsConfiguration kafkaStreamsConfiguration() {
        Map<String, Object> props = new HashMap();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG,"kafka-streams-app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapAddress);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG, LogAndContinueExceptionHandler.class);
        props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG,1000);
        props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG,0);

        return new KafkaStreamsConfiguration(props);
    }

    @Bean
    NewTopic inputLocationTopic() {
        return TopicBuilder.name(KafkaTopics.inputLocationTopic)
                .partitions(2)
                .replicas(1)
                .build();
    }

    @Bean
    NewTopic rideEventTopic() {
        return TopicBuilder.name(KafkaTopics.rideEventTopic)
                .partitions(2)
                .replicas(1)
                .build();
    }

    @Bean
    NewTopic outputLocationTopic() {
        return TopicBuilder.name(KafkaTopics.outputLocationTopic)
                .partitions(2)
                .replicas(1)
                .build();
    }
}
