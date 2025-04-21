package sia.plants.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sia.plants.model.CareHistory;
import sia.plants.model.plant.Plant;

import java.util.UUID;
import org.springframework.stereotype.Repository;


@Repository
public interface CareHIstoryRepository extends JpaRepository<CareHistory, Integer> {
}
