package sia.plants.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Entity
@Table(name = "user_task_view")
@IdClass(UserTaskViewId.class)
public class UserTaskView {

    @Setter
    @Id
    @Column(name = "taskid")
    private Integer taskId;

    @Setter
    @Id
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


    @Column(name = "care_taker")
    private String careTaker;

    @Column(name = "location_name")
    private String locationName;

    public void setCareTaker(String careTaker) {
        this.careTaker = careTaker;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }



    @Column(name = "notified")
    private boolean notified;

    // Getters and Setters

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDueDate(Timestamp dueDate) {
        this.dueDate = dueDate;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public void setPlantId(Integer plantId) {
        this.plantId = plantId;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public void setPlantSpecies(String plantSpecies) {
        this.plantSpecies = plantSpecies;
    }
}
