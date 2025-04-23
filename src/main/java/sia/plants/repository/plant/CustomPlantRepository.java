package sia.plants.repository.plant;

import java.util.List;
import java.util.UUID;

public interface CustomPlantRepository {
    void createSmartPlantWithImages(
            String plantName,
            String species,
            UUID organizationId,
            String locationName,
            String categoryName,
            String humidity,
            String lightRequirements,
            String water,
            String temperatureRange,
            List<String> imageUrls
    );
}

