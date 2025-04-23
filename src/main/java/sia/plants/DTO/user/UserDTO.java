package sia.plants.DTO.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {
    private String mail;
    private String name;
    private Boolean admin;
    private String organizationName;
}
