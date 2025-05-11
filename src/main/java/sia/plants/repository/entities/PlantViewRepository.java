package sia.plants.repository.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import sia.plants.entities.PlantView;

public interface PlantViewRepository extends JpaRepository<PlantView, Integer> {
    PlantView findPlantViewByPlantId(Integer plantId);
}
