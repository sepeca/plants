package sia.plants.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import sia.plants.model.Task;
import org.springframework.stereotype.Repository;


@Repository
public interface TaskRepository extends JpaRepository<Task, Integer>{
}
