package sia.plants.DTO.user;

import lombok.Data;

import java.util.UUID;

@Data
public class ToggleAdminRequest {

    private UUID userId;
    private Boolean isAdmin;
}
