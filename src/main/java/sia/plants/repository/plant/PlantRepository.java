package sia.plants.repository.plant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;
import sia.plants.model.plant.Plant;


import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;


@Repository
public interface PlantRepository extends JpaRepository<Plant, Integer>, CustomPlantRepository {
    @Query("SELECT p.plantId FROM Plant p WHERE p.organization.organizationId = :orgId")
    List<Integer> findAllPlantIdsByOrganizationId(@Param("orgId") UUID orgId);
}