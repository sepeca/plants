package sia.plants.DTO.careHistory;

import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
public class CareHistoryDTO {
    private Integer careId;
    private String type;
    private String description;
    private Timestamp timestamp;
    private UUID userId;
}
