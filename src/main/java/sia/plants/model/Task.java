package sia.plants.model;
import jakarta.persistence.*;
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

    @Column(name = "completed", nullable = false)
    private Boolean completed = false;

    @Column(name = "due_date", nullable = false)
    private Timestamp dueDate;

    @Column(name = "text")
    private String text;

    @ManyToOne
    @JoinColumn(name = "plantid", nullable = false)
    private Plant plant;

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }
}