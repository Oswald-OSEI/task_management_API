package taskmanagement.taskmanagementapi.taskmanagementservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import taskmanagement.taskmanagementapi.taskmanagementservice.entity.TaskEntity;
import taskmanagement.taskmanagementapi.taskmanagementservice.enums.Priority;
import taskmanagement.taskmanagementapi.taskmanagementservice.enums.Status;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    Page<TaskEntity> findAllByCreator_Id(Pageable pageable, long creatorId);
    Page<TaskEntity> findAllByPriorityAndCreator_Id(Priority priority, long creatorId,  Pageable pageable);
    Page<TaskEntity> findAllByCreator_IdAndStatus(long creatorId, Status status, Pageable pageable);
    Page<TaskEntity>findAllByDeadlineLessThanEqualAndCreator_Id(LocalDate deadline, long creatorId, Pageable pageable);
    Optional<TaskEntity> findByIdAndCreator_Id(long taskId, long creatorId);
    Page<TaskEntity> findAllByDeadlineGreaterThanEqualAndCreator_Id(LocalDate deadline, long creatorId, Pageable pageable);
    Page<TaskEntity> findAllByDeadlineAndCreator_Id(LocalDate deadline, long creatorId, Pageable pageable);
}
