package sia.plants.DTO.user;

import lombok.Data;

@Data
public class CreateUserWithOrganizationRequest {
    private String name;
    private String email;
    private String password;

    private String organizationName;

}
