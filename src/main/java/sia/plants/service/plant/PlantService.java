package sia.plants.service.plant;

import sia.plants.DTO.plant.CreatePlantRequest;
import sia.plants.model.CareHistory;
import sia.plants.model.plant.Plant;
import sia.plants.model.plant.PlantInfo;

import java.util.List;
import java.util.UUID;

public interface PlantService {
    void createSmartPlantWithImages (CreatePlantRequest request);
    List<Integer> getAllPlantsByUserId(UUID userId);
    PlantInfo updatePlantInfo();
    CareHistory addCareEvent ();
}
