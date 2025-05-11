package sia.plants.DTO.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.boot.context.properties.bind.Name;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
public class CreateTaskRequest {
    @Name("text")
    private String description;
    private Timestamp dueDate;
    private Integer plantId;
    @JsonProperty("time")
    private Integer finishDate;
    private List<String> emails;
    private Boolean me;
}