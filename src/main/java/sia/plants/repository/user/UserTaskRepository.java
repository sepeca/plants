package sia.plants.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sia.plants.model.Task;
import sia.plants.model.user.UserTask;
import sia.plants.model.user.UserTaskId;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;


@Repository
public interface UserTaskRepository extends JpaRepository<UserTask, UserTaskId> {




}