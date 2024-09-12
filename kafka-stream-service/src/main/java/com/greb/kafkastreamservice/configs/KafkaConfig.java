package com.greb.locationservice.configs;

import com.greb.locationservice.serdes.JsonSerde;
import com.greb.locationservice.services.LocationConsumerService;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.streams.StreamsConfig;
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
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;
    private Logger log = LoggerFactory.getLogger(LocationConsumerService.class);

    @Bean(name= KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    KafkaStreamsConfiguration kafkaStreamsConfiguration() {
        Map<String, Object> props = new HashMap();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG,"location-app-id");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapAddress);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG,1000);
        props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG,0);

        return new KafkaStreamsConfiguration(props);
    }

    @Bean
    public KafkaListenerErrorHandler kafkaErrorHandler() {
        return (m, e) -> {
            if(e!=null){
                log.error("Kakfa error. Detail {}", e.getMessage());
            }
            return null;
        };
    }


    @Bean
    NewTopic locationTopic() {
        return TopicBuilder.name("location-topic")
                .partitions(2)
                .replicas(1)
                .build();
    }

    @Bean
    NewTopic distanceTopic() {
        return TopicBuilder.name("distance-topic")
                .partitions(2)
                .replicas(1)
                .build();
    }
}
