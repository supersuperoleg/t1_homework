package com.rakhimov.homework.service;

import com.rakhimov.homework.aspect.annotation.ExceptionLog;
import com.rakhimov.homework.aspect.annotation.ExecutionTimeLog;
import com.rakhimov.homework.aspect.annotation.ResultLog;
import com.rakhimov.homework.dto.TaskDto;
import com.rakhimov.homework.entity.Task;
import com.rakhimov.homework.enums.TaskStatus;
import com.rakhimov.homework.exception.InvalidTaskStatusException;
import com.rakhimov.homework.exception.TaskCreationException;
import com.rakhimov.homework.exception.TaskNotFoundException;
import com.rakhimov.homework.kafka.KafkaTaskProducer;
import com.rakhimov.homework.repository.TaskRepository;
import com.rakhimov.homework.util.TaskMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class TaskService {

    @Value("${kafka.task-topic}")
    private String topic;

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final KafkaTaskProducer kafkaTaskProducer;
    private final NotificationService notificationService;

    @ResultLog
    @ExceptionLog
    @ExecutionTimeLog
    public TaskDto createTask(TaskDto taskDto) {
        try {
            Task task = taskMapper.toEntity(taskDto);
            Task createdTask = taskRepository.save(task);
            return taskMapper.toDto(createdTask);
        } catch (Exception e) {
            String error = String.format("Failed to create task by dto: %s", taskDto);
            throw new TaskCreationException(error, e);
        }
    }

    @ResultLog
    @ExceptionLog
    @ExecutionTimeLog
    public Optional<TaskDto> getTaskById(Long id) {
        return taskRepository.findById(id)
                .map(taskMapper::toDto);
    }

    @ResultLog
    @ExceptionLog
    public TaskDto updateTask(Long id, TaskDto taskDto) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isEmpty()) {
            throw new TaskNotFoundException("Task not found with id " + id);
        }
        Task taskToUpdate = optionalTask.get();

        TaskStatus statusByDto;
        try {
            statusByDto = TaskStatus.valueOf(taskDto.getStatus());
        } catch (IllegalArgumentException e) {
            throw new InvalidTaskStatusException("Invalid status: " + taskDto.getStatus());
        }

        TaskStatus currentStatus = taskToUpdate.getStatus();

        taskToUpdate.setTitle(taskDto.getTitle());
        taskToUpdate.setDescription(taskDto.getDescription());
        taskToUpdate.setUserId(taskDto.getUserId());
        taskToUpdate.setStatus(statusByDto);

        Task updatedTask = taskRepository.save(taskToUpdate);

        // Если статус изменился, отправляем в Kafka
        if (currentStatus == null || !currentStatus.equals(statusByDto)) {
            kafkaTaskProducer.sendTo(topic, taskDto);
        }

        return taskMapper.toDto(updatedTask);
    }

    @ResultLog
    @ExceptionLog
    @ExecutionTimeLog
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException("Task not found with id " + id);
        }
        taskRepository.deleteById(id);
    }

    @ResultLog
    @ExceptionLog
    @ExecutionTimeLog
    public List<TaskDto> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());
    }

    public void notifyAboutUpdate(List<Task> tasks) {
        if (tasks.isEmpty()) {
            return;
        }
        String result = formatTaskList(tasks);
        try {
            notificationService.sendEmail("List of updated tasks", result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return;
        }
        log.info("Письмо отправлено");
    }

    public String formatTaskList(List<Task> tasks) {
        if (tasks == null || tasks.isEmpty()) {
            return "No tasks available.";
        }

        return tasks.stream()
                .map(task -> task.toString())
                .collect(Collectors.joining("\n", "Updated tasks:\n", ""));
    }


}