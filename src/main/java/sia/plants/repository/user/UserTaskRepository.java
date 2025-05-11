package sia.plants.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import sia.plants.model.user.UserTask;
import sia.plants.model.user.UserTaskId;

import org.springframework.stereotype.Repository;


@Repository
public interface UserTaskRepository extends JpaRepository<UserTask, UserTaskId> {




}