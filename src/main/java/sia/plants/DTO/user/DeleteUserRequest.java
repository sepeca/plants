package sia.plants.DTO.user;

import lombok.Data;

import java.util.UUID;

@Data
public class DeleteUserRequest {
    private String userId;
}
