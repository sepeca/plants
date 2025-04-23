package sia.plants.DTO.user;

import lombok.Data;
import java.util.UUID;

@Data
public class OrgMemberResponse {
    private UUID userId;
    private String name;
    private String email;
}