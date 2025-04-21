package sia.plants.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sia.plants.model.CareHistory;
import sia.plants.model.plant.Plant;

import java.util.UUID;

public interface CareHIstoryRepository extends JpaRepository<CareHistory, Integer> {
}
