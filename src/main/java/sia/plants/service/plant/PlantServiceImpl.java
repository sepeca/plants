package sia.plants.service.plant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sia.plants.DTO.plant.CreatePlantRequest;
import sia.plants.model.CareHistory;
import sia.plants.model.plant.Plant;
import sia.plants.model.plant.PlantInfo;
import sia.plants.repository.plant.PlantRepository;

@Service
public class PlantServiceImpl implements PlantService{
    private final PlantRepository plantRepository;
    public PlantServiceImpl(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    @Override
    @Transactional
    public void createSmartPlantWithImages (CreatePlantRequest request){
        try {
            plantRepository.createSmartPlantWithImages(
                    request.getPlantName(),
                    request.getSpecies(),
                    request.getOrganizationId(),
                    request.getLocationName(),
                    request.getCategoryName(),
                    request.getHumidity(),
                    request.getLightRequirements(),
                    request.getWater(),
                    request.getTemperatureRange(),
                    request.getImageUrls()
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to create plant: " + e.getMessage(), e);
        }
    }
    @Override
    public PlantInfo updatePlantInfo(){
        return null;
    };
    @Override
    public CareHistory addCareEvent (){
        return null;
    };
}
