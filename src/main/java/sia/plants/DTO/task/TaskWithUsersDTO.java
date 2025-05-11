package sia.plants.DTO.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import jdk.jfr.Label;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;
@Data
public class TaskWithUsersDTO {
    //TODO pridat locationName
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
    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Timestamp getDueDate() {
        return dueDate;
    }

    public void setDueDate(Timestamp dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getPlantId() {
        return plantId;
    }

    public void setPlantId(Integer plantId) {
        this.plantId = plantId;
    }

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public String getPlantSpecies() {
        return plantSpecies;
    }

    public void setPlantSpecies(String plantSpecies) {
        this.plantSpecies = plantSpecies;
    }

    public List<String> getAssignedUsers() {
        return assignedUsers;
    }

    public void setAssignedUsers(List<String> assignedUsers) {
        this.assignedUsers = assignedUsers;
    }



    public void setCareTaker(String careTaker) {
        this.careTaker = careTaker;
    }
}
