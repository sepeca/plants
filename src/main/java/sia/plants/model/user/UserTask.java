package sia.plants.model.user;
import jakarta.persistence.*;
import sia.plants.model.Task;


@Entity
@Table(name = "user_task")
@IdClass(UserTaskId.class)
public class UserTask {

    @Id
    @ManyToOne
    @JoinColumn(name = "taskid", nullable = false)
    private Task task;

    @Id
    @ManyToOne
    @JoinColumn(name = "userid", nullable = false)
    private User user;

    @Column(name = "notified", nullable = false)
    private Boolean notified;

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getNotified() {
        return notified;
    }

    public void setNotified(Boolean notified) {
        this.notified = notified;
    }

}
