package sia.plants.DTO.task;



import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class FinishTasksRequest {

    @JsonProperty("task_ids")
    private List<Integer> taskIds;

}
