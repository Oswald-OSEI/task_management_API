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
import taskmanagement.taskmanagementapi.taskmanagementservice.enums.Priority;
import taskmanagement.taskmanagementapi.taskmanagementservice.enums.Status;
import taskmanagement.taskmanagementapi.taskmanagementservice.exception.TaskManagementException;
import taskmanagement.taskmanagementapi.taskmanagementservice.repository.TaskRepository;
import taskmanagement.taskmanagementapi.userservice.entity.UserEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskFilterServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private TaskFilterService taskFilterService;

    private final String TEST_TOKEN = "test-token";
    private final Long TEST_USER_ID = 1L;
    private final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
    private final Pageable pageable = Pageable.unpaged();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(taskFilterService, "secretString", SECRET);
    }

    @Test
    void filterTasksByStatus_SuccessfulCase() {
        // Setup
        TaskEntity task = new TaskEntity();
        task.setStatus(Status.IN_PROGRESS);
        UserEntity creator = new UserEntity();
        creator.setId(TEST_USER_ID);
        task.setCreator(creator);
        Page<TaskEntity> taskPage = new PageImpl<>(List.of(task));

        when(tokenService.extractId(TEST_TOKEN, SECRET)).thenReturn(TEST_USER_ID);
        when(taskRepository.findAllByCreator_IdAndStatus(TEST_USER_ID, Status.IN_PROGRESS, pageable))
                .thenReturn(taskPage);

        // Execute
        Page<TaskDto> result = taskFilterService.filterTasksByStatus(TEST_TOKEN, Status.IN_PROGRESS, pageable);

        // Verify
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(Status.IN_PROGRESS, result.getContent().getFirst().status());
    }

    @Test
    void filterTasksByStatus_ThrowsException_WhenTokenInvalid() {
        // Setup
        when(tokenService.extractId(TEST_TOKEN, SECRET)).thenThrow(new RuntimeException());

        // Execute & Verify
        assertThrows(TaskManagementException.class,
            () -> taskFilterService.filterTasksByStatus(TEST_TOKEN, Status.IN_PROGRESS, pageable));
    }

    @Test
    void filterTasksByPriority_SuccessfulCase() {
        // Setup
        TaskEntity task = new TaskEntity();
        task.setPriority(Priority.HIGH);
        UserEntity creator = new UserEntity();
        creator.setId(TEST_USER_ID);
        task.setCreator(creator);
        Page<TaskEntity> taskPage = new PageImpl<>(List.of(task));

        when(tokenService.extractId(TEST_TOKEN, SECRET)).thenReturn(TEST_USER_ID);
        when(taskRepository.findAllByPriorityAndCreator_Id(Priority.HIGH, TEST_USER_ID, pageable))
                .thenReturn(taskPage);

        // Execute
        Page<TaskDto> result = taskFilterService.filterTasksByPriority(TEST_TOKEN, Priority.HIGH, pageable);

        // Verify
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(Priority.HIGH, result.getContent().get(0).priority());
    }

    @Test
    void filterTasksByDeadlineOnOrBefore_SuccessfulCase() {
        // Setup
        LocalDate deadline = LocalDate.now();
        TaskEntity task = new TaskEntity();
        task.setDeadline(deadline);
        UserEntity creator = new UserEntity();
        creator.setId(TEST_USER_ID);
        task.setCreator(creator);
        Page<TaskEntity> taskPage = new PageImpl<>(List.of(task));

        when(tokenService.extractId(TEST_TOKEN, SECRET)).thenReturn(TEST_USER_ID);
        when(taskRepository.findAllByDeadlineLessThanEqualAndCreator_Id(deadline, TEST_USER_ID, pageable))
                .thenReturn(taskPage);

        // Execute
        Page<TaskDto> result = taskFilterService.filterTasksByDeadlineOnOrBefore(TEST_TOKEN, deadline, pageable);

        // Verify
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(deadline, result.getContent().getFirst().deadline());
    }

    @Test
    void filterTasksByDeadlineOnOrAfter_SuccessfulCase() {
        // Setup
        LocalDate deadline = LocalDate.now();
        TaskEntity task = new TaskEntity();
        task.setDeadline(deadline);
        UserEntity creator = new UserEntity();
        creator.setId(TEST_USER_ID);
        task.setCreator(creator);
        Page<TaskEntity> taskPage = new PageImpl<>(List.of(task));

        when(tokenService.extractId(TEST_TOKEN, SECRET)).thenReturn(TEST_USER_ID);
        when(taskRepository.findAllByDeadlineGreaterThanEqualAndCreator_Id(deadline, TEST_USER_ID, pageable))
                .thenReturn(taskPage);

        // Execute
        Page<TaskDto> result = taskFilterService.filterTasksByDeadlineOnOrAfter(TEST_TOKEN, deadline, pageable);

        // Verify
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(deadline, result.getContent().getFirst().deadline());
    }

    @Test
    void filterTasksByDeadlineOn_SuccessfulCase() {
        // Setup
        LocalDate deadline = LocalDate.now();
        TaskEntity task = new TaskEntity();
        task.setDeadline(deadline);
        UserEntity creator = new UserEntity();
        creator.setId(TEST_USER_ID);
        task.setCreator(creator);
        Page<TaskEntity> taskPage = new PageImpl<>(List.of(task));

        when(tokenService.extractId(TEST_TOKEN, SECRET)).thenReturn(TEST_USER_ID);
        when(taskRepository.findAllByDeadlineAndCreator_Id(deadline, TEST_USER_ID, pageable))
                .thenReturn(taskPage);

        // Execute
        Page<TaskDto> result = taskFilterService.filterTasksByDeadlineOn(TEST_TOKEN, deadline, pageable);

        // Verify
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(deadline, result.getContent().getFirst().deadline());
    }
}