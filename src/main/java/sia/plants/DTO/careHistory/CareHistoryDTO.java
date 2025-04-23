package sia.plants.DTO.careHistory;

import lombok.Data;

import java.sql.Timestamp;


@Data
public class CareHistoryDTO {
    private Integer careHistoryId;
    private Timestamp careDate;
    private String imageUrl;
    private String notes;

    private String careTypeName;

    private String userEmail;
    private String userName;
}
