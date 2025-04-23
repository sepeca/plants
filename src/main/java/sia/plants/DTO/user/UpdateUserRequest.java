package sia.plants.DTO.user;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String name;
    private String newPassword;
}