package com.rakhimov.homework.controller;

import com.rakhimov.homework.aspect.annotation.RestMethodCallLog;
import com.rakhimov.homework.dto.ResponseDto;
import com.rakhimov.homework.dto.TaskDto;
import com.rakhimov.homework.exception.TaskCreationException;
import com.rakhimov.homework.exception.TaskNotFoundException;
import com.rakhimov.homework.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    @RestMethodCallLog
    @PostMapping
    public ResponseDto<TaskDto> createTask(@RequestBody TaskDto taskDTO) {
        try {
            TaskDto createdTaskDto = taskService.createTask(taskDTO);

            return ResponseDto.success(createdTaskDto, "Task created successfully");

        } catch (TaskCreationException e) {
            return ResponseDto.error("Failed to create task", e.getMessage());
        } catch (Exception ex) {
            return ResponseDto.error("An unexpected error occurred", ex.getMessage());
        }
    }

    @RestMethodCallLog
    @GetMapping("/{id}")
    public ResponseDto<TaskDto> getTaskById(@PathVariable Long id) {
        try {
            if (id == 0L) {
                throw new IllegalArgumentException("Task ID cannot be 0");
            }
            Optional<TaskDto> optionalTaskDto = taskService.getTaskById(id);
            if (optionalTaskDto.isEmpty()) {
                return ResponseDto.error("Task not found", "No task found with ID " + id);
            }
            TaskDto taskDto = optionalTaskDto.get();
            return ResponseDto.success(taskDto, "Task found successfully");

        } catch (IllegalArgumentException e) {
            return ResponseDto.error("Invalid Task ID", e.getMessage());
        } catch (Exception ex) {
            return ResponseDto.error("An unexpected error occurred", ex.getMessage());
        }
    }

    @RestMethodCallLog
    @PutMapping("/{id}")
    public ResponseDto<TaskDto> updateTask(@PathVariable Long id, @RequestBody TaskDto taskDto) {
        try {
            if (id == 0L) {
                throw new IllegalArgumentException("Task ID cannot be 0");
            }
            TaskDto updatedTaskDto = taskService.updateTask(id, taskDto);

            return ResponseDto.success(updatedTaskDto, "Task updated successfully");

        } catch (IllegalArgumentException e) {
            return ResponseDto.error("Invalid Task ID", e.getMessage());
        } catch (TaskNotFoundException e) {
            return ResponseDto.error("Task not found", e.getMessage());
        } catch (Exception ex) {
            return ResponseDto.error("An unexpected error occurred", ex.getMessage());
        }
    }

    @RestMethodCallLog
    @DeleteMapping("/{id}")
    public ResponseDto<Void> deleteTask(@PathVariable Long id) {
        try {
            if (id == 0) {
                throw new IllegalArgumentException("Task ID cannot be 0");
            }
            taskService.deleteTask(id);

            return ResponseDto.success(null, "Task deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseDto.error("Invalid Task ID", e.getMessage());
        } catch (TaskNotFoundException e) {
            return ResponseDto.error("Task not found", e.getMessage());
        } catch (Exception ex) {
            return ResponseDto.error("An unexpected error occurred", ex.getMessage());
        }
    }

    @RestMethodCallLog
    @GetMapping
    public ResponseDto<List<TaskDto>> getAllTasks() {
        try {
            List<TaskDto> tasks = taskService.getAllTasks();

            if (tasks.isEmpty()) {
                return ResponseDto.error("No tasks found", "There are no tasks available");
            }
            return ResponseDto.success(tasks, "Tasks retrieved successfully");
        } catch (Exception e) {
            return ResponseDto.error("An unexpected error occurred", e.getMessage());
        }
    }
}
