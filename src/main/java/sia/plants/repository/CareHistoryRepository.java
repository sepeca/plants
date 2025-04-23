package sia.plants.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sia.plants.model.CareHistory;

import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CareHistoryRepository extends JpaRepository<CareHistory, Integer> {
    List<CareHistory> findAllByPlant_PlantId(Integer plantId);
}
