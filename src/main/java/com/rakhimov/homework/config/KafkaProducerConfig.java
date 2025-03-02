package com.rakhimov.homework.config;

import com.rakhimov.homework.dto.TaskDto;
import com.rakhimov.homework.kafka.KafkaTaskProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Configuration
public class KafkaProducerConfig {

    private final KafkaConfigProperties kafkaConfigProperties;

    @Bean
    public ProducerFactory<String, TaskDto> producerTaskFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfigProperties.getServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, false);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean("task")
    public KafkaTemplate<String, TaskDto> kafkaTemplate(ProducerFactory<String, TaskDto> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public KafkaTaskProducer producerTasks(KafkaTemplate<String, TaskDto> template) {
        template.setDefaultTopic(kafkaConfigProperties.getTaskTopic());
        return new KafkaTaskProducer(template);
    }
}
