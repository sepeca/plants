package sia.plants.model.user;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.*;
public class UserTaskId implements Serializable {
    private Integer task;
    private UUID user;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserTaskId that = (UserTaskId) o;
        return Objects.equals(task, that.task) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(task, user);
    }
}
