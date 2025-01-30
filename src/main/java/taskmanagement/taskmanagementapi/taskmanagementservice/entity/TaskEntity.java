package taskmanagement.taskmanagementapi.taskmanagementservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.engine.internal.Cascade;
import taskmanagement.taskmanagementapi.taskmanagementservice.enums.Priority;
import taskmanagement.taskmanagementapi.taskmanagementservice.enums.Status;
import taskmanagement.taskmanagementapi.userservice.entity.UserEntity;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TaskEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String title;

    private Priority priority;

    private Status status;

    private LocalDate deadline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private UserEntity creator;
}
