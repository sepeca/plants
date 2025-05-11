package sia.plants.service.plant;

import sia.plants.DTO.plant.CreatePlantRequest;
import sia.plants.DTO.plant.PlantAllDTO;
import sia.plants.entities.PlantDetailView;


import java.util.List;
import java.util.UUID;

public interface PlantService {
    void createSmartPlantWithImages (CreatePlantRequest request, List<String> imageUrls);
    void deletePlant(Integer plantId);
    List<PlantAllDTO> getAllPlantByOrgId(UUID organizationId);
    PlantDetailView getPlantDetail(Integer plantId);
}
