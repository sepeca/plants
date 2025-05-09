package sia.plants.DTO.task;



import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class FinishTasksRequest {

    @JsonProperty("task_ids")
    private List<Integer> taskIds;

    public List<Integer> getTaskIds() {
        return taskIds;
    }

    public void setTaskIds(List<Integer> taskIds) {
        this.taskIds = taskIds;
    }
}
