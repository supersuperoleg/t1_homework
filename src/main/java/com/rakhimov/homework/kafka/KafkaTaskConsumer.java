package com.rakhimov.homework.kafka;

import com.rakhimov.homework.entity.Task;
import com.rakhimov.homework.util.TaskMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import com.rakhimov.homework.dto.TaskDto;
import com.rakhimov.homework.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaTaskConsumer {
    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @KafkaListener(id = "group-id",
            topics = "${kafka.task-topic}",
            containerFactory = "kafkaListenerContainerFactory")
    public void listener(@Payload List<TaskDto> messageList,
                         Acknowledgment ack,
                         @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                         @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        log.debug("Task consumer: обработка новых сообщений");
        try {
            List<Task> tasks = messageList.stream()
                    .map(taskMapper::toEntity)
                    .toList();
            // отправляем письма
            taskService.notifyAboutUpdate(tasks);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            ack.acknowledge();
        }
        log.debug("Task consumer: записи обработаны");
    }
}
