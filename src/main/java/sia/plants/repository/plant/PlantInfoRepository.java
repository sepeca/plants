package sia.plants.repository.plant;

import org.springframework.data.jpa.repository.JpaRepository;
import sia.plants.model.plant.PlantInfo;




public interface PlantInfoRepository extends JpaRepository<PlantInfo, Integer> {
}
