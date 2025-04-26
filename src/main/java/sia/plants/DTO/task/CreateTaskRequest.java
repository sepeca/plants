package sia.plants.DTO.task;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
public class CreateTaskRequest {
    private String text;
    private Timestamp dueDate;
    private Integer plantId;
    private List<UUID> userIds;
    private Boolean me;
}