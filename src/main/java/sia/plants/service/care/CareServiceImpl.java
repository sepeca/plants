package sia.plants.service.care;

import org.springframework.stereotype.Service;
import sia.plants.DTO.careHistory.CareHistoryDTO;
import sia.plants.model.plant.Plant;
import sia.plants.repository.CareHistoryRepository;
import sia.plants.repository.plant.PlantRepository;
import sia.plants.security.JwtService;

import java.util.List;
import java.util.UUID;

@Service
public class CareServiceImpl implements CareService{
    private final PlantRepository plantRepository;
    private final CareHistoryRepository careHistoryRepository;

    public CareServiceImpl(PlantRepository plantRepository,
                       CareHistoryRepository careHistoryRepository,
                       JwtService jwtService) {
        this.plantRepository = plantRepository;
        this.careHistoryRepository = careHistoryRepository;

    }
    public List<CareHistoryDTO> getCareHistoryByPlantId(Integer plantId) {

        return careHistoryRepository.findAllByPlant_PlantId(plantId).stream()
                .map(ch -> {
                    CareHistoryDTO dto = new CareHistoryDTO();
                    dto.setCareHistoryId(ch.getCareHistoryId());
                    dto.setCareDate(ch.getCareDate());
                    dto.setImageUrl(ch.getImageUrl());
                    dto.setNotes(ch.getNotes());

                    dto.setCareTypeName(ch.getCareType().getName());

                    dto.setUserEmail(ch.getUser().getEmail());
                    dto.setUserName(ch.getUser().getName());
                    return dto;
                }).toList();
    }
    public List<CareHistoryDTO> getCareHistoryByUserId(UUID userId){
        return careHistoryRepository.findAllByUser_UserId(userId).stream()
                .map(ch -> {
                    CareHistoryDTO dto = new CareHistoryDTO();
                    dto.setCareHistoryId(ch.getCareHistoryId());
                    dto.setCareDate(ch.getCareDate());
                    dto.setImageUrl(ch.getImageUrl());
                    dto.setNotes(ch.getNotes());

                    dto.setCareTypeName(ch.getCareType().getName());

                    dto.setUserEmail(ch.getUser().getEmail());
                    dto.setUserName(ch.getUser().getName());
                    return dto;
                }).toList();
    }
}
