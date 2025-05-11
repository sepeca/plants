package sia.plants.model.user;
import jakarta.persistence.*;
import lombok.Getter;
import sia.plants.model.Task;


@Entity
@Table(name = "user_task")
@IdClass(UserTaskId.class)
public class UserTask {

    @Getter
    @Id
    @ManyToOne
    @JoinColumn(name = "taskid", nullable = false)
    private Task task;

    @Getter
    @Id
    @ManyToOne
    @JoinColumn(name = "userid", nullable = false)
    private User user;

    @Column(name = "notified", nullable = false)
    private Boolean notified;

    public void setTask(Task task) {
        this.task = task;
    }

    public void setUser(User user) {
        this.user = user;
    }



    public void setNotified(Boolean notified) {
        this.notified = notified;
    }

}
