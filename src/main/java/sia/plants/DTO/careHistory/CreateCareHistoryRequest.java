package sia.plants.DTO.careHistory;

import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;


@Data
public class CreateCareHistoryRequest {

    private String imageUrl;
    private String notes;

    private Integer plantId;
    private Integer careTypeId;
    private UUID userId;
}
