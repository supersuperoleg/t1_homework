package com.rakhimov.homework.service;

import com.rakhimov.homework.entity.Task;
import com.rakhimov.homework.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTask_Success() {
        // Arrange
        Task inputTask = new Task();
        inputTask.setId(1L);
        inputTask.setTitle("Test Task");

        Task savedTask = new Task();
        savedTask.setId(1L);
        savedTask.setTitle("Test Task");

        when(taskRepository.save(inputTask)).thenReturn(savedTask);

        // Act
        Task result = taskService.createTask(inputTask);

        // Assert
        assertNotNull(result);
        assertEquals(inputTask.getTitle(), result.getTitle());
        assertEquals(inputTask.getId(), result.getId());
        verify(taskRepository, times(1)).save(inputTask);
    }

    @Test
    public void testCreateTask_NullInput() {
        // Arrange
        Task inputTask = null;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            taskService.createTask(inputTask);
        });

        assertEquals("Task cannot be null", exception.getMessage());
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    public void testGetTaskById_Success() {
        // Arrange
        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);
        task.setTitle("Test Task");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        // Act
        Optional<Task> result = taskService.getTaskById(taskId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(taskId, result.get().getId());
        assertEquals("Test Task", result.get().getTitle());
        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    public void testGetTaskById_TaskNotFound() {
        // Arrange
        Long taskId = 999999L; // ID, которого нет в базе данных

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // Act
        Optional<Task> result = taskService.getTaskById(taskId);

        // Assert
        assertFalse(result.isPresent());
        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    public void testDeleteTask_Success() {
        // Arrange
        Long taskId = 1L;

        when(taskRepository.existsById(taskId)).thenReturn(true);

        // Act
        boolean result = taskService.deleteTask(taskId);

        // Assert
        assertTrue(result);
        verify(taskRepository, times(1)).existsById(taskId);
        verify(taskRepository, times(1)).deleteById(taskId);
    }

    @Test
    public void testDeleteTask_TaskNotFound() {
        // Arrange
        Long taskId = 999L;

        when(taskRepository.existsById(taskId)).thenReturn(false);

        // Act
        boolean result = taskService.deleteTask(taskId);

        // Assert
        assertFalse(result);
        verify(taskRepository, times(1)).existsById(taskId);
        verify(taskRepository, never()).deleteById(taskId);
    }

    @Test
    public void testGetAllTasks_Success() {
        // Arrange
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task 1");

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task 2");

        List<Task> tasks = Arrays.asList(task1, task2);

        when(taskRepository.findAll()).thenReturn(tasks);

        // Act
        List<Task> result = taskService.getAllTasks();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(task1));
        assertTrue(result.contains(task2));
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllTasks_EmptyList() {
        // Arrange
        when(taskRepository.findAll()).thenReturn(java.util.Collections.emptyList());

        // Act
        List<Task> result = taskService.getAllTasks();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(taskRepository, times(1)).findAll();
    }

}