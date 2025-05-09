package sia.plants.repository.plant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import sia.plants.model.plant.Plant;
import sia.plants.model.user.User;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;


@Repository
public interface PlantRepository extends JpaRepository<Plant, Integer>, CustomPlantRepository {
    List<Plant> findAllByOrganization_OrganizationId(UUID organizationId);

}