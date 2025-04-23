package sia.plants.service.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sia.plants.DTO.user.CreateUserRequest;
import sia.plants.DTO.user.CreateUserWithOrganizationRequest;
import sia.plants.DTO.user.OrgMemberResponse;
import sia.plants.DTO.user.UpdateUserRequest;
import sia.plants.model.user.User;
import sia.plants.repository.user.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface UserService {
    User authenticate(String email, String rawPassword);
    User createUser(CreateUserRequest request);
    User getUserById(String userId);
    User registerUserWithOrganization(CreateUserWithOrganizationRequest request);
    User updateCurrentUser(String userId, UpdateUserRequest request);

    List<OrgMemberResponse> getOrgMembers(UUID orgId);
}

