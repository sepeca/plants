package sia.plants.DTO.task;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class TaskResponse {
    private Integer taskId;
    private Boolean completed;
    private Timestamp dueDate;
    private String text;
    private TaskPlantDTO plant;
}
