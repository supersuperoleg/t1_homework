package com.rakhimov.homework.service;

import com.rakhimov.homework.aspect.annotation.ExecutionTimeLog;
import com.rakhimov.homework.entity.Task;
import com.rakhimov.homework.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @ExecutionTimeLog
    public Task createTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        return taskRepository.save(task);
    }

    @ExecutionTimeLog
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Optional<Task> updateTask(Long id, Task updatedTask) {
        return taskRepository.findById(id).map(task -> {
            task.setTitle(updatedTask.getTitle());
            task.setDescription(updatedTask.getDescription());
            task.setUserId(updatedTask.getUserId());
            return taskRepository.save(task);
        });
    }

    @ExecutionTimeLog
    public boolean deleteTask(Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @ExecutionTimeLog
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
}