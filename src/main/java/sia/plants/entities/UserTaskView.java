package sia.plants.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "user_task_view")
public class UserTaskView {

    @Id
    @Column(name = "taskid")
    private Integer taskId;

    @Column(name = "userid")
    private UUID userId;

    @Column(name = "username")
    private String username;

    @Column(name = "due_date")
    private Timestamp dueDate;

    @Column(name = "completed")
    private Boolean completed;

    @Column(name = "taskdescription")
    private String taskDescription;

    @Column(name = "plantid")
    private Integer plantId;

    @Column(name = "plant_name")
    private String plantName;

    @Column(name = "plant_species")
    private String plantSpecies;

    // Getters and Setters

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getDueDate() {
        return dueDate;
    }

    public void setDueDate(Timestamp dueDate) {
        this.dueDate = dueDate;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
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
}
