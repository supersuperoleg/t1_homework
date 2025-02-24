package com.rakhimov.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rakhimov.homework.entity.Task;
import com.rakhimov.homework.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
    }

    @Test
    public void testCreateTask_Success() throws Exception {
        // Arrange
        Task inputTask = new Task();
        inputTask.setId(null);
        inputTask.setTitle("Test Task");

        Task createdTask = new Task();
        createdTask.setId(1L);
        createdTask.setTitle("Test Task");

        when(taskService.createTask(inputTask)).thenReturn(createdTask);

        // Act & Assert
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(inputTask)))
                .andExpect(status().isOk()) // HTTP 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test Task"));

        verify(taskService, times(1)).createTask(inputTask);
    }

    // Вспомогательный метод для преобразования объекта в JSON
    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetTaskById_Success() throws Exception {
        // Arrange
        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);
        task.setTitle("Test Task");

        when(taskService.getTaskById(taskId)).thenReturn(Optional.of(task));

        // Act & Assert
        mockMvc.perform(get("/tasks/{id}", taskId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // HTTP 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(taskId)) // Проверяем ID задачи
                .andExpect(jsonPath("$.title").value("Test Task")); // Проверяем название задачи

        verify(taskService, times(1)).getTaskById(taskId);
    }

    @Test
    public void testGetTaskById_TaskNotFound() throws Exception {
        // Arrange
        Long taskId = 999L;

        when(taskService.getTaskById(taskId)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/tasks/{id}", taskId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(taskService, times(1)).getTaskById(taskId);
    }

    @Test
    public void testUpdateTask_Success() throws Exception {
        // Arrange
        Long taskId = 1L;
        Task updatedTask = new Task();
        updatedTask.setId(taskId);
        updatedTask.setTitle("Updated Task");

        when(taskService.updateTask(taskId, updatedTask)).thenReturn(Optional.of(updatedTask));

        // Act & Assert
        mockMvc.perform(put("/tasks/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedTask))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // HTTP 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(taskId))
                .andExpect(jsonPath("$.title").value("Updated Task"));

        verify(taskService, times(1)).updateTask(taskId, updatedTask);
    }

    @Test
    public void testUpdateTask_TaskNotFound() throws Exception {
        // Arrange
        Long taskId = 999L;
        Task taskToUpdate = new Task();
        taskToUpdate.setId(taskId);
        taskToUpdate.setTitle("Nonexistent Task");

        when(taskService.updateTask(taskId, taskToUpdate)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(put("/tasks/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(taskToUpdate))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(taskService, times(1)).updateTask(taskId, taskToUpdate);
    }

    @Test
    public void testDeleteTask_Success() throws Exception {
        // Arrange
        Long taskId = 1L;

        when(taskService.deleteTask(taskId)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(delete("/tasks/{id}", taskId))
                .andExpect(status().isNoContent());

        verify(taskService, times(1)).deleteTask(taskId);
    }

    @Test
    public void testDeleteTask_TaskNotFound() throws Exception {
        // Arrange
        Long taskId = 999999L;

        when(taskService.deleteTask(taskId)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(delete("/tasks/{id}", taskId))
                .andExpect(status().isNotFound());

        verify(taskService, times(1)).deleteTask(taskId);
    }

    @Test
    public void testGetAllTasks_Success() throws Exception {
        // Arrange
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task 1");

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task 2");

        List<Task> tasks = Arrays.asList(task1, task2);

        when(taskService.getAllTasks()).thenReturn(tasks);

        // Act & Assert
        mockMvc.perform(get("/tasks")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Task 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].title").value("Task 2"));

        verify(taskService, times(1)).getAllTasks();
    }

    @Test
    public void testGetAllTasks_EmptyList() throws Exception {
        // Arrange
        when(taskService.getAllTasks()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/tasks")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isEmpty());

        verify(taskService, times(1)).getAllTasks();
    }

}