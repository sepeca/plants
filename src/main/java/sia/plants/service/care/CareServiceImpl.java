package sia.plants.service.care;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sia.plants.DTO.careHistory.CareHistoryDTO;
import sia.plants.DTO.careHistory.CreateCareHistoryRequest;
import sia.plants.exception.NotFoundException;
import sia.plants.model.CareHistory;
import sia.plants.model.CareType;
import sia.plants.model.plant.Plant;
import sia.plants.model.user.User;
import sia.plants.repository.CareHistoryRepository;
import sia.plants.repository.plant.PlantRepository;
import sia.plants.service.imageHandler.ImageHandler;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Service
public class CareServiceImpl implements CareService{
    private final PlantRepository plantRepository;
    private final CareHistoryRepository careHistoryRepository;
    private final ImageHandler imageHandler;
    private final EntityManager entityManager;

    public CareServiceImpl(PlantRepository plantRepository,
                           CareHistoryRepository careHistoryRepository,
                           ImageHandler imageHandler,
                           EntityManager entityManager) {
        this.plantRepository = plantRepository;
        this.careHistoryRepository = careHistoryRepository;
        this.imageHandler = imageHandler;
        this.entityManager = entityManager;

    }
    @Override
    @Transactional
    public void createCareHistory(CreateCareHistoryRequest request,
                                  Plant plant,
                                  User user,
                                  CareType careType,
                                  String imageUrl) {

        CareHistory careHistory = new CareHistory();
        careHistory.setCareDate(new Timestamp(System.currentTimeMillis()));
        careHistory.setImageUrl(imageUrl);
        careHistory.setNotes(request.getNotes());
        careHistory.setPlant(plant);
        careHistory.setUser(user);
        careHistory.setUserName(user.getName());
        careHistory.setCareType(careType);

        careHistoryRepository.save(careHistory);
    }

    @Override
    public List<CareHistoryDTO> getCareHistoryByPlantId(Integer plantId) {

        return careHistoryRepository.findAllByPlant_PlantId(plantId).stream()
                .map(ch -> {
                    CareHistoryDTO dto = new CareHistoryDTO();
                    dto.setCareHistoryId(ch.getCareHistoryId());
                    dto.setCareDate(ch.getCareDate());
                    dto.setImageUrl(ch.getImageUrl());
                    dto.setNotes(ch.getNotes());

                    dto.setCareTypeName(ch.getCareType().getName());
                    if(ch.getUser() != null){
                    dto.setUserEmail(ch.getUser().getEmail());
                    dto.setUserName(ch.getUser().getName());}
                    else{
                        dto.setUserName(ch.getUserName());
                    }
                    return dto;
                }).toList();
    }

    @Override
    @Transactional
    public void deleteCareHistory(Integer careHistoryId, UUID userId, boolean isAdmin) {
        CareHistory history = careHistoryRepository.findById(careHistoryId)
                .orElseThrow(() -> new NotFoundException("Care history not found"));
        if(!isAdmin && history.getUser().getUserId() != userId){
            throw new RuntimeException("You are not allowed to delete this careHistory");
        }

        imageHandler.deleteFileFromDisk(history.getImageUrl());

        careHistoryRepository.deleteDirectly(history.getCareHistoryId());
    }
}
