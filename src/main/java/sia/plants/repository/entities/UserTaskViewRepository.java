package sia.plants.repository.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import sia.plants.entities.UserTaskView;
import sia.plants.entities.UserTaskViewId;

import java.util.List;
import java.util.UUID;

public interface UserTaskViewRepository extends JpaRepository<UserTaskView, UserTaskViewId> {
    List<UserTaskView> findAllByUserId(UUID userId);
    List<UserTaskView> findAllByUserIdAndTaskIdIn(UUID userId, List<Integer> taskIds);

}