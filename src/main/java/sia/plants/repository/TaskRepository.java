package sia.plants.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import sia.plants.model.Task;

public interface TaskRepository extends JpaRepository<Task, Integer>{
}
