package com.rakhimov.homework.controller;

import com.rakhimov.homework.aspect.annotation.ExceptionLog;
import com.rakhimov.homework.aspect.annotation.RestMethodCallLog;
import com.rakhimov.homework.aspect.annotation.ResultLog;
import com.rakhimov.homework.entity.Task;
import com.rakhimov.homework.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @ExceptionLog
    @ResultLog
    @RestMethodCallLog
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task createdTask = taskService.createTask(task);
        return ResponseEntity.ok(createdTask);
    }

    @ExceptionLog
    @ResultLog
    @RestMethodCallLog
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        if (id == 0L) {
            throw new IllegalArgumentException("Task ID cannot be 0");
        }
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @ExceptionLog
    @ResultLog
    @RestMethodCallLog
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
        return taskService.updateTask(id, task)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @ExceptionLog
    @ResultLog
    @RestMethodCallLog
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        if (id == 0) {
            throw new IllegalArgumentException("Task ID cannot be 0");
        }
        if (taskService.deleteTask(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @ExceptionLog
    @ResultLog
    @RestMethodCallLog
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }
}
