package sia.plants.service.care;

import sia.plants.DTO.careHistory.CareHistoryDTO;

import java.util.List;
import java.util.UUID;

public interface CareService {
    List<CareHistoryDTO> getCareHistoryByPlantId(Integer plantId);
    List<CareHistoryDTO> getCareHistoryByUserId(UUID userId);
}
