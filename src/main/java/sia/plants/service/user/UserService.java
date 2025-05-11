package sia.plants.service.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sia.plants.DTO.user.*;
import sia.plants.entities.UserOrgView;
import sia.plants.model.user.User;
import sia.plants.repository.user.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface UserService {
    User authenticate(String email, String rawPassword);
    User createUser(CreateUserRequest request);
    UserOrgView getCurrentUserInfo(UUID userId);
    User getUserById(String userId);
    User registerUserWithOrganization(CreateUserWithOrganizationRequest request);
    User updateCurrentUser(String userId, UpdateUserRequest request);
    String getEmailByUserId(UUID userId);
    List<OrgMemberResponse> getOrgMembers(UUID orgId);
    List<UUID> getOrgMembersIds(UUID orgId);
    void toggleAdmin(UUID orgId, ToggleAdminRequest request);
    void deleteUser(UUID userId);
}

