package sia.plants.repository.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import sia.plants.entities.UserTaskView;

import java.util.List;
import java.util.UUID;

public interface UserTaskViewRepository extends JpaRepository<UserTaskView, Integer> {
    List<UserTaskView> findAllByUserId(UUID userId);

}