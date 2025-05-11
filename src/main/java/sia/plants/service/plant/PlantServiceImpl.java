package sia.plants.service.plant;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sia.plants.DTO.plant.CreatePlantRequest;
import sia.plants.DTO.plant.PlantAllDTO;
import sia.plants.entities.PlantDetailView;
import sia.plants.entities.PlantView;
import sia.plants.model.CareHistory;
import sia.plants.model.plant.Image;
import sia.plants.model.plant.Plant;
import sia.plants.model.plant.PlantInfo;
import sia.plants.repository.CareHistoryRepository;
import sia.plants.repository.entities.PlantViewRepository;
import sia.plants.repository.plant.CustomPlantRepository;
import sia.plants.repository.plant.ImageRepository;
import sia.plants.repository.plant.PlantDetailViewRepository;
import sia.plants.repository.plant.PlantRepository;
import sia.plants.service.imageHandler.ImageHandler;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PlantServiceImpl implements PlantService{
    private final PlantRepository plantRepository;
    private final ImageRepository imageRepository;
    private final PlantViewRepository plantViewRepository;
    private final CareHistoryRepository careHistoryRepository;
    private final CustomPlantRepository customPlantRepository;
    private final PlantDetailViewRepository plantDetailViewRepository;
    private final ImageHandler imageHandler;
    public PlantServiceImpl(PlantRepository plantRepository,
                            PlantDetailViewRepository plantDetailViewRepository,
                            ImageRepository imageRepository,
                            CareHistoryRepository careHistoryRepository,
                            ImageHandler imageHandler,
                            PlantViewRepository plantViewRepository,
                            @Qualifier("customPlantRepositoryImpl") CustomPlantRepository customPlantRepository) {
        this.plantRepository = plantRepository;
        this.imageRepository = imageRepository;
        this.plantDetailViewRepository = plantDetailViewRepository;
        this.careHistoryRepository = careHistoryRepository;
        this.imageHandler = imageHandler;
        this.customPlantRepository = customPlantRepository;
        this.plantViewRepository = plantViewRepository;
    }

    @Override
    @Transactional
    public void createSmartPlantWithImages (CreatePlantRequest request, List<String> imageUls){
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
                    imageUls
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to create plant: " + e.getMessage(), e);
        }
    }
    @Override
    public void deletePlant(Integer plantId){
        List<Image> images = imageRepository.findAllByPlant_PlantId(plantId);
        List<String> urls = images.stream().map(Image::getUrl).toList();
        for(String url : urls){
            imageHandler.deleteFileFromDisk(url);
        }
        List<CareHistory> careHistories = careHistoryRepository.findAllByPlant_PlantId(plantId);
        for(CareHistory ch : careHistories){
            imageHandler.deleteFileFromDisk(ch.getImageUrl());
        }


        customPlantRepository.deletePlantCascade(plantId);
    }

    @Override
    public List<PlantAllDTO> getAllPlantByOrgId(UUID organizationId) {
        List<PlantAllDTO> dtoList = new ArrayList<>();
        List<Integer> plantIds = plantRepository.findAllPlantIdsByOrganizationId(organizationId);
        for(int id : plantIds){
            PlantView plantView = plantViewRepository.findPlantViewByPlantId(id);
            PlantAllDTO dto = new PlantAllDTO();
            dto.setPlantId(plantView.getPlantId());
            dto.setPlatName(plantView.getPlantName());
            dto.setCategoryName(plantView.getCategoryName());
            dto.setLocationName(plantView.getLocationName());
            dtoList.add(dto);
        }


        return dtoList;
    }
    @Override
    public PlantDetailView getPlantDetail(Integer plantId){
        return plantDetailViewRepository.findById(plantId)
                .orElseThrow(() -> new RuntimeException("Plant not found with id: " + plantId));    }

}
