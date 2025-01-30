package taskmanagement.taskmanagementapi.taskmanagementservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import taskmanagement.taskmanagementapi.taskmanagementservice.dto.TaskDto;
import taskmanagement.taskmanagementapi.taskmanagementservice.enums.Priority;
import taskmanagement.taskmanagementapi.taskmanagementservice.enums.Status;
import taskmanagement.taskmanagementapi.taskmanagementservice.service.TaskService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TaskControllerTest {
    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTask_ShouldCreateNewTasks() {
        // Arrange
        List<TaskDto> tasks = Arrays.asList(
                new TaskDto(null, "Task 1", Priority.HIGH, Status.IN_PROGRESS, LocalDate.now(), 1L)
        );
        String token = "Bearer token";
        when(taskService.addTask(tasks, token)).thenReturn("Tasks created");

        // Act
        var response = taskController.createTask(tasks, token);

        // Assert
        assertEquals(HttpStatus.CREATED.value(), response.getBody().statusCode());
        assertEquals("Successful", response.getBody().message());
        verify(taskService).addTask(tasks, token);
    }

    @Test
    void updateTask_ShouldUpdateExistingTask() {
        // Arrange
        TaskDto task = new TaskDto(1L, "Updated Task", Priority.HIGH, Status.IN_PROGRESS, LocalDate.now(), 1L);
        String token = "Bearer token";
        when(taskService.updateTask(token, task)).thenReturn(task);

        // Act
        var response = taskController.updateTask(task, token);

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getBody().statusCode());
        verify(taskService).updateTask(token, task);
    }

    @Test
    void getAllTasks_ShouldReturnAllTasks() {
        // Arrange
        String token = "Bearer token";
        Pageable pageable = PageRequest.of(0, 10);
        List<TaskDto> tasksList = Arrays.asList(
                new TaskDto(1L, "Task 1", Priority.HIGH, Status.COMPLETED, LocalDate.now(), 1L)
        );
        Page<TaskDto> expectedTasks = new PageImpl<>(tasksList, pageable, tasksList.size());
        when(taskService.getAllTasks(pageable, token)).thenReturn(expectedTasks);

        // Act
        var response = taskController.getAllTasks(token, pageable);

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getBody().statusCode());
        assertEquals("Successful", response.getBody().message());
        verify(taskService).getAllTasks(pageable, token);
    }

    @Test
    void getTaskById_ShouldReturnSpecificTask() {
        // Arrange
        long taskId = 1L;
        String token = "Bearer token";
        TaskDto expectedTask = new TaskDto(taskId, "Task 1", Priority.HIGH, Status.COMPLETED, LocalDate.now(), 1L);
        when(taskService.getTask(taskId, token)).thenReturn(expectedTask);

        // Act
        var response = taskController.getTaskById(taskId, token);

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getBody().statusCode());
        assertEquals("Successful", response.getBody().message());
        verify(taskService).getTask(taskId, token);
    }

    @Test
    void deleteTask_ShouldDeleteSpecificTask() {
        // Arrange
        long taskId = 1L;
        String token = "Bearer token";
        when(taskService.deleteTask(taskId, token)).thenReturn("Deleted Successfully");

        // Act
        var response = taskController.deleteTask(taskId, token);

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getBody().statusCode());
        assertEquals("Deleted Successfully", response.getBody().message());
        verify(taskService).deleteTask(taskId, token);
    }
}

