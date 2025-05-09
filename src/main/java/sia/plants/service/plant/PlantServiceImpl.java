package sia.plants.service.plant;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sia.plants.DTO.plant.CreatePlantRequest;
import sia.plants.DTO.plant.PlantAllDTO;
import sia.plants.entities.PlantDetailView;
import sia.plants.model.CareHistory;
import sia.plants.model.plant.Plant;
import sia.plants.model.plant.PlantInfo;
import sia.plants.repository.plant.CustomPlantRepository;
import sia.plants.repository.plant.PlantDetailViewRepository;
import sia.plants.repository.plant.PlantRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PlantServiceImpl implements PlantService{
    private final PlantRepository plantRepository;
    private final CustomPlantRepository customPlantRepository;
    private final PlantDetailViewRepository plantDetailViewRepository;
    public PlantServiceImpl(PlantRepository plantRepository,
                            PlantDetailViewRepository plantDetailViewRepository,
                            @Qualifier("customPlantRepositoryImpl") CustomPlantRepository customPlantRepository) {
        this.plantRepository = plantRepository;
        this.plantDetailViewRepository = plantDetailViewRepository;
        this.customPlantRepository = customPlantRepository;
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
    public void deletePlant(Integer plantId){
        customPlantRepository.deletePlantCascade(plantId);
    }
    @Override
    public List<Integer> getAllPlantsByUserId(UUID userId){return null;}
    @Override
    public PlantInfo updatePlantInfo(){
        return null;
    };
    @Override
    public CareHistory addCareEvent (){
        return null;
    };
    @Override
    public List<PlantAllDTO> getAllPlantByOrgId(UUID organizationId) {
        List<PlantAllDTO> dtoList = new ArrayList<>();

        for (Plant plant : plantRepository.findAllByOrganization_OrganizationId(organizationId)) {
            PlantAllDTO dto = new PlantAllDTO();
            dto.setPlantId(plant.getPlantId());
            dto.setPlatName(plant.getName());
            dto.setCategoryName(plant.getPlantCategory().getName());
            dto.setLocationName(plant.getLocation().getName());
            dtoList.add(dto); // ← добавляем в список
        }

        return dtoList; // ← возвращаем DTO, а не сущности
    }
    @Override
    public PlantDetailView getPlantDetail(Integer plantId){
        return plantDetailViewRepository.findById(plantId)
                .orElseThrow(() -> new RuntimeException("Plant not found with id: " + plantId));    }
}
