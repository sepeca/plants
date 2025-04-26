package sia.plants.service.care;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sia.plants.DTO.careHistory.CareHistoryDTO;
import sia.plants.DTO.careHistory.CreateCareHistoryRequest;
import sia.plants.DTO.careHistory.UpdateCareHistoryRequest;
import sia.plants.exception.NotFoundException;
import sia.plants.model.CareHistory;
import sia.plants.model.CareType;
import sia.plants.model.plant.Plant;
import sia.plants.model.user.User;
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
    @Override
    @Transactional
    public void createCareHistory(CreateCareHistoryRequest request,
                                  Plant plant,
                                  User user,
                                  CareType careType) {

        CareHistory careHistory = new CareHistory();
        careHistory.setCareDate(request.getCareDate());
        careHistory.setImageUrl(request.getImageUrl());
        careHistory.setNotes(request.getNotes());
        careHistory.setPlant(plant);
        careHistory.setUser(user);
        careHistory.setCareType(careType);

        careHistoryRepository.save(careHistory); // Триггер проверит принадлежность к организации
    }
    @Override
    public CareHistory updateCareHistory(Integer id, UpdateCareHistoryRequest request, UUID currentUserId, boolean isAdmin) {
        CareHistory history = careHistoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Care history not found"));


        UUID authorId = history.getUser().getUserId();
        if (!authorId.equals(currentUserId) && !isAdmin) {
            throw new IllegalArgumentException("You are not authorized to update this care history");
        }

        String imageUrl = request.getImageUrl();
        if (imageUrl != null) {
            history.setImageUrl(request.getImageUrl());
        }
        String notes = request.getNotes();
        if (notes != null) {
            history.setNotes(request.getNotes());
        }


        return careHistoryRepository.save(history);
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

                    dto.setUserEmail(ch.getUser().getEmail());
                    dto.setUserName(ch.getUser().getName());
                    return dto;
                }).toList();
    }
    @Override
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
