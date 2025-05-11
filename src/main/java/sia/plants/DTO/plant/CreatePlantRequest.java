package sia.plants.DTO.plant;

import jdk.jfr.Name;
import lombok.Data;
import java.util.UUID;

@Data
public class CreatePlantRequest {
    private String plantName;
    private String species;
    private UUID organizationId;
    @Name("location_name")
    private String locationName;
    @Name("category_name")
    private String categoryName;

    private String humidity;
    private String lightRequirements;
    private String water;
    private String temperatureRange;


}