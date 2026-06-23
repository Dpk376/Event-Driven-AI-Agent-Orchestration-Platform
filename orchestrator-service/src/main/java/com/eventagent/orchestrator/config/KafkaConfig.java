package com.eventagent.orchestrator.config;

import com.eventagent.common.exception.NonRetryableException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.ExponentialBackOff;

@Configuration
public class KafkaConfig {

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(
            ConsumerFactory<String, Object> consumerFactory,
            KafkaTemplate<String, Object> kafkaTemplate) {

        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);

        // MANUAL manual offset commit is required for exactly-once-like processing semantics
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);

        // Dead Letter Queue setup
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate);
        
        // Exponential backoff for retryable exceptions
        ExponentialBackOff backOff = new ExponentialBackOff(1000L, 2.0);
        backOff.setMaxInterval(10000L);
        backOff.setMaxAttempts(3);

        DefaultErrorHandler errorHandler = new DefaultErrorHandler(recoverer, backOff);
        
        // Do not retry for non-retryable exceptions (e.g. Validation, Deduplication failure)
        errorHandler.addNotRetryableExceptions(NonRetryableException.class);
        errorHandler.addNotRetryableExceptions(IllegalArgumentException.class);

        factory.setCommonErrorHandler(errorHandler);

        return factory;
    }
}
