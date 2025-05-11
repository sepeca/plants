package sia.plants.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sia.plants.model.CareHistory;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface CareHistoryRepository extends JpaRepository<CareHistory, Integer> {
    List<CareHistory> findAllByPlant_PlantId(Integer plantId);
    List<CareHistory> findAllByUser_UserId(UUID userId);
    @Modifying
    @Query("DELETE FROM CareHistory ch WHERE ch.careHistoryId = :id")
    void deleteDirectly(@Param("id") Integer id);
}
