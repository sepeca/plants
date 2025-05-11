package sia.plants.service.care;

import sia.plants.DTO.careHistory.CareHistoryDTO;
import sia.plants.DTO.careHistory.CreateCareHistoryRequest;
import sia.plants.model.CareType;
import sia.plants.model.plant.Plant;
import sia.plants.model.user.User;

import java.util.List;
import java.util.UUID;

public interface CareService {
    void createCareHistory(CreateCareHistoryRequest request,
                           Plant plant,
                           User user,
                           CareType careType,
                           String imageUrl);
    void deleteCareHistory(Integer careHistoryId, UUID deleterId, boolean isAdmin);
    List<CareHistoryDTO> getCareHistoryByPlantId(Integer plantId);

}
