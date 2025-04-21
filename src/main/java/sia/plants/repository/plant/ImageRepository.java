package sia.plants.repository.plant;

import org.springframework.data.jpa.repository.JpaRepository;
import sia.plants.model.plant.Image;
import sia.plants.model.plant.Plant;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;


@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAllByPlant_PlantId(Integer plantId);
}
