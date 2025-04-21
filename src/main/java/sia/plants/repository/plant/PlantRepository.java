package sia.plants.repository.plant;

import org.springframework.data.jpa.repository.JpaRepository;
import sia.plants.model.plant.Plant;
import sia.plants.model.user.User;

import java.util.UUID;
import org.springframework.stereotype.Repository;


@Repository
public interface PlantRepository extends JpaRepository<Plant, Integer> {
}