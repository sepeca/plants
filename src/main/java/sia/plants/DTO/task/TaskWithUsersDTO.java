package sia.plants.DTO.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import jdk.jfr.Label;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;
@Data
public class TaskWithUsersDTO {
    private String locationName;
    private Integer taskId;
    private String taskDescription;
    private Boolean completed;
    @JsonProperty("taskDate")
    private Timestamp dueDate;
    private Integer plantId;
    private String plantName;
    private String plantSpecies;
    public  String careTaker;
    private boolean notified;

    private List<String> assignedUsers;

}
