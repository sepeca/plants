package sia.plants.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sia.plants.model.Task;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface TaskRepository extends JpaRepository<Task, Integer>{
    @Query("SELECT ut.task FROM UserTask ut WHERE ut.user.userId = :userId")
    List<Task> findTasksByUserId(@Param("userId") UUID userId);


    @Query("SELECT DISTINCT ut.task FROM UserTask ut WHERE ut.user.organization.organizationId = :orgId")
    List<Task> findTasksByOrganizationId(@Param("orgId") UUID orgId);
}
