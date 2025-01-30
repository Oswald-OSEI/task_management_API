package taskmanagement.taskmanagementapi.taskmanagementservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;
import taskmanagement.taskmanagementapi.common.security.jwt.TokenService;
import taskmanagement.taskmanagementapi.taskmanagementservice.dto.TaskDto;
import taskmanagement.taskmanagementapi.taskmanagementservice.entity.TaskEntity;
import taskmanagement.taskmanagementapi.taskmanagementservice.exception.TaskCreationException;
import taskmanagement.taskmanagementapi.taskmanagementservice.repository.TaskRepository;
import taskmanagement.taskmanagementapi.userservice.entity.UserEntity;
import taskmanagement.taskmanagementapi.userservice.repository.UserRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static taskmanagement.taskmanagementapi.taskmanagementservice.enums.Priority.HIGH;
import static taskmanagement.taskmanagementapi.taskmanagementservice.enums.Status.IN_PROGRESS;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private TaskService taskService;

    private final String TEST_TOKEN = "test-token";
    private final long TEST_USER_ID = 1L;
    private final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(taskService, "secretString", SECRET);
    }

    @Test
    void addTask_SuccessfulCase() {
        // Setup
        TaskDto taskDto = new TaskDto(1L, "Test Task", HIGH, IN_PROGRESS, LocalDate.now(), null);
        List<TaskDto> taskDtos = List.of(taskDto);
        UserEntity user = new UserEntity();
        user.setId(TEST_USER_ID);
        when(tokenService.extractId(TEST_TOKEN, SECRET)).thenReturn(TEST_USER_ID);
        when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.of(user));
        when(taskRepository.saveAll(any())).thenReturn(new ArrayList<>());

        // Execute
        String result = taskService.addTask(taskDtos, TEST_TOKEN);

        // Verify
        assertEquals("Tasks added successfully", result);
        verify(taskRepository).saveAll(any());
    }

    @Test
    void addTask_EmptyList_ThrowsException() {
        // Execute & Verify
        assertThrows(TaskCreationException.class,
                () -> taskService.addTask(new ArrayList<>(), TEST_TOKEN));
    }

    @Test
    void getAllTasks_SuccessfulCase() {
        // Setup
        Pageable pageable = Pageable.unpaged();
        TaskEntity task = new TaskEntity();
        task.setId(1L);
        UserEntity creator = new UserEntity();
        creator.setId(TEST_USER_ID);
        task.setCreator(creator);
        List<TaskEntity> tasks = List.of(task);
        Page<TaskEntity> taskPage = new PageImpl<>(tasks);

        when(tokenService.extractId(TEST_TOKEN, SECRET)).thenReturn(TEST_USER_ID);
        when(taskRepository.findAllByCreator_Id( pageable, TEST_USER_ID)).thenReturn(taskPage);

        // Execute
        Page<TaskDto> result = taskService.getAllTasks(pageable, TEST_TOKEN);

        // Verify
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void updateTask_SuccessfulCase() {
        // Setup
        UserEntity user = new UserEntity();
        user.setId(TEST_USER_ID);
        TaskDto taskDto = new TaskDto(1L, "Updated Task", HIGH, IN_PROGRESS, LocalDate.now(), user.getId());
        TaskEntity existingTask = new TaskEntity();
        existingTask.setId(1L);
        existingTask.setCreator(user);

        when(tokenService.extractId(TEST_TOKEN, SECRET)).thenReturn(TEST_USER_ID);
        when(taskRepository.findByIdAndCreator_Id(1L, TEST_USER_ID)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(TaskEntity.class))).thenReturn(existingTask);

        // Execute
        TaskDto result = taskService.updateTask(TEST_TOKEN, taskDto);

        // Verify
        assertNotNull(result);
        verify(taskRepository).save(any(TaskEntity.class));
    }

    @Test
    void getTask_SuccessfulCase() {
        // Setup
        TaskEntity task = new TaskEntity();
        task.setId(1L);
        UserEntity user = new UserEntity();
        user.setId(TEST_USER_ID);
        task.setCreator(user);

        when(tokenService.extractId(TEST_TOKEN, SECRET)).thenReturn(TEST_USER_ID);
        when(taskRepository.findByIdAndCreator_Id(1L, TEST_USER_ID)).thenReturn(Optional.of(task));

        // Execute
        TaskDto result = taskService.getTask(1L, TEST_TOKEN);

        // Verify
        assertNotNull(result);
    }

    @Test
    void deleteTask_SuccessfulCase() {
        // Setup
        TaskEntity task = new TaskEntity();
        UserEntity creator = new UserEntity();
        creator.setId(TEST_USER_ID);
        task.setCreator(creator);

        when(tokenService.extractId(TEST_TOKEN, SECRET)).thenReturn(TEST_USER_ID);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // Execute
        String result = taskService.deleteTask(1L, TEST_TOKEN);

        // Verify
        assertEquals("task deleted", result);
        verify(taskRepository).delete(task);
    }
}