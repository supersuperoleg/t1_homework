package com.rakhimov.homework.kafka;

import com.rakhimov.homework.dto.TaskDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaTaskProducer {

    private final KafkaTemplate<String, TaskDto> template;

    public void send(TaskDto taskDto) {
        try {
            template.sendDefault(taskDto).get();
            template.flush();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void sendTo(String topic, TaskDto taskDto) {
        try {
            template.send(topic, taskDto).get();
            template.flush();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
