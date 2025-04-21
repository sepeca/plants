package sia.plants.repository.plant;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import sia.plants.model.plant.PlantCategory;


@Repository
public interface PlantCategoryRepository extends JpaRepository<PlantCategory, Integer> {
}
