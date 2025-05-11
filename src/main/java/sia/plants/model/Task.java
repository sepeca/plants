package sia.plants.model;
import jakarta.persistence.*;
import lombok.Setter;
import sia.plants.model.plant.Plant;

import java.sql.Timestamp;
import java.util.*;
@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "taskid")
    private Integer taskId;

    @Setter
    @Column(name = "completed", nullable = false)
    private Boolean completed = false;

    @Setter
    @Column(name = "due_date", nullable = false)
    private Timestamp dueDate;

    @Setter
    @Column(name = "text")
    private String text;


    @Column(name = "care_taker")
    private String careTaker;
    @Setter
    @ManyToOne
    @JoinColumn(name = "plantid", nullable = false)
    private Plant plant;

    public Integer getTaskId() {
        return taskId;
    }



    public Boolean getCompleted() {
        return completed;
    }

    public Timestamp getDueDate() {
        return dueDate;
    }

    public String getText() {
        return text;
    }

    public Plant getPlant() {
        return plant;
    }


    public String getCareTaker() {
        return careTaker;
    }



}