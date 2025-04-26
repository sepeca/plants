package sia.plants.service.care;

import sia.plants.DTO.careHistory.CareHistoryDTO;
import sia.plants.DTO.careHistory.CreateCareHistoryRequest;
import sia.plants.DTO.careHistory.UpdateCareHistoryRequest;
import sia.plants.model.CareHistory;
import sia.plants.model.CareType;
import sia.plants.model.plant.Plant;
import sia.plants.model.user.User;

import java.util.List;
import java.util.UUID;

public interface CareService {
    void createCareHistory(CreateCareHistoryRequest request,
                           Plant plant,
                           User user,
                           CareType careType);
    List<CareHistoryDTO> getCareHistoryByPlantId(Integer plantId);
    List<CareHistoryDTO> getCareHistoryByUserId(UUID userId);
    CareHistory updateCareHistory(Integer id, UpdateCareHistoryRequest request, UUID currentUserId, boolean isAdmin);
}
