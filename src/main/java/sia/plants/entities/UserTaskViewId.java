package sia.plants.entities;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class UserTaskViewId implements Serializable {
    private Integer taskId;
    private UUID userId;

    public UserTaskViewId() {}

    public UserTaskViewId(Integer taskId, UUID userId) {
        this.taskId = taskId;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserTaskViewId that)) return false;
        return Objects.equals(taskId, that.taskId) &&
                Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId, userId);
    }
}
