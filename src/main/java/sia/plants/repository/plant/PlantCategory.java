package sia.plants.repository.plant;

import org.springframework.data.jpa.repository.JpaRepository;
import sia.plants.model.plant.Plant;

import java.util.UUID;

public interface PlantCategory extends JpaRepository<PlantCategory, Integer> {
}
