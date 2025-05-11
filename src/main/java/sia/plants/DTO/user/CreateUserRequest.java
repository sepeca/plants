package sia.plants.DTO.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateUserRequest {
    private String email;
    @JsonProperty("name")
    private String name;
    private String password;

    private Boolean isAdmin = false;
    private UUID organizationId;
}
