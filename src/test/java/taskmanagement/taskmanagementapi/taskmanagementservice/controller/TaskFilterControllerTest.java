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
import taskmanagement.taskmanagementapi.taskmanagementservice.service.TaskFilterService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TaskFilterControllerTest {
    @Mock
    private TaskFilterService taskFilterService;

    @InjectMocks
    private TaskFilterController taskFilterController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void filterTaskByStatus_ShouldReturnFilteredTasks() {
        // Arrange
        Status status = Status.IN_PROGRESS;
        String token = "Bearer token";
        Pageable pageable = PageRequest.of(0, 10);
        List<TaskDto> tasksList = Arrays.asList(
                new TaskDto(1L, "Task 1", Priority.HIGH, Status.IN_PROGRESS, LocalDate.now(), 1L)
        );
        Page<TaskDto> expectedTasks = new PageImpl<>(tasksList, pageable, tasksList.size());
        when(taskFilterService.filterTasksByStatus(token, status, pageable)).thenReturn(expectedTasks);

        // Act
        var response = taskFilterController.filterTaskByStatus(status, token, pageable);

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getBody().statusCode());
        assertEquals("retrieved successfully", response.getBody().message());
        verify(taskFilterService).filterTasksByStatus(token, status, pageable);
    }

    @Test
    void filterTaskByPriority_ShouldReturnFilteredTasks() {
        // Arrange
        Priority priority = Priority.HIGH;
        String token = "Bearer token";
        Pageable pageable = PageRequest.of(0, 10);
        List<TaskDto> tasksList = Arrays.asList(
                new TaskDto(1L, "Task 1", Priority.HIGH, Status.PENDING, LocalDate.now(), 1L)
        );
        Page<TaskDto> expectedTasks = new PageImpl<>(tasksList, pageable, tasksList.size());
        when(taskFilterService.filterTasksByPriority(token, priority, pageable)).thenReturn(expectedTasks);

        // Act
        var response = taskFilterController.filterTaskByPriority(priority, token, pageable);

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getBody().statusCode());
        assertEquals("retrieved successfully", response.getBody().message());
        verify(taskFilterService).filterTasksByPriority(token, priority, pageable);
    }

    @Test
    void filterByDeadlineOnOrBefore_ShouldReturnFilteredTasks() {
        // Arrange
        LocalDate deadline = LocalDate.now();
        String token = "Bearer token";
        Pageable pageable = PageRequest.of(0, 10);
        List<TaskDto> tasksList = Arrays.asList(
                new TaskDto(1L, "Task 1", Priority.HIGH, Status.PENDING, deadline, 1L)
        );
        Page<TaskDto> expectedTasks = new PageImpl<>(tasksList, pageable, tasksList.size());
        when(taskFilterService.filterTasksByDeadlineOnOrBefore(token, deadline, pageable)).thenReturn(expectedTasks);

        // Act
        var response = taskFilterController.filterByDeadlineOnOrBefore(deadline, token, pageable);

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getBody().statusCode());
        assertEquals("retrieved successfully", response.getBody().message());
        verify(taskFilterService).filterTasksByDeadlineOnOrBefore(token, deadline, pageable);
    }
}