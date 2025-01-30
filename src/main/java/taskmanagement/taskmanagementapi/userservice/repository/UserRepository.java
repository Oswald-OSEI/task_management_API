package taskmanagement.taskmanagementapi.userservice.repository;
import org.springframework.data.repository.CrudRepository;
import taskmanagement.taskmanagementapi.userservice.entity.UserEntity;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
}
