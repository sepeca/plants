package sia.plants.DTO.user;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String username;
    private String newPassword;
}