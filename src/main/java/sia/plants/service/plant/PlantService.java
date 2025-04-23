package sia.plants.service.plant;

import sia.plants.DTO.plant.CreatePlantRequest;
import sia.plants.model.CareHistory;
import sia.plants.model.plant.Plant;
import sia.plants.model.plant.PlantInfo;

public interface PlantService {
    void createSmartPlantWithImages (CreatePlantRequest request);
    PlantInfo updatePlantInfo();
    CareHistory addCareEvent ();
}
