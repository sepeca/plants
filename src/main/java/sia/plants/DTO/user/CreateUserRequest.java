package sia.plants.DTO.user;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateUserRequest {
    private String email;
    private String name;
    private String password;

    private Boolean isAdmin = false;
    private UUID organizationId;
}
