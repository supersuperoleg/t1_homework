package com.rakhimov.homework.service;

import com.rakhimov.homework.aspect.annotation.ExceptionLog;
import com.rakhimov.homework.aspect.annotation.ExecutionTimeLog;
import com.rakhimov.homework.aspect.annotation.ResultLog;
import com.rakhimov.homework.dto.TaskDto;
import com.rakhimov.homework.entity.Task;
import com.rakhimov.homework.exception.TaskCreationException;
import com.rakhimov.homework.exception.TaskNotFoundException;
import com.rakhimov.homework.repository.TaskRepository;
import com.rakhimov.homework.util.TaskMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

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
        return taskRepository.findById(id)
                .map(task -> {
                    task.setTitle(taskDto.title());
                    task.setDescription(taskDto.description());
                    task.setUserId(taskDto.userId());

                    Task updatedTask = taskRepository.save(task);

                    return taskMapper.toDto(updatedTask);
                })
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id " + id));
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
}