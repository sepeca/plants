package sia.plants.service.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sia.plants.DTO.user.*;
import sia.plants.entities.UserOrgView;
import sia.plants.exception.NotFoundException;
import sia.plants.model.user.User;
import sia.plants.repository.entities.UserOrgViewRepository;
import sia.plants.repository.user.UserRepository;
import sia.plants.model.Organization;
import sia.plants.repository.OrganizationRepository;
import sia.plants.repository.user.UserRepositoryCustom;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OrganizationRepository organizationRepository;
    private final UserOrgViewRepository userOrgViewRepository;
    private final UserRepositoryCustom userRepositoryCustom;
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           OrganizationRepository organizationRepository,
                           UserOrgViewRepository userOrgViewRepository,
                           UserRepositoryCustom userRepositoryCustom) {
        this.userRepository = userRepository;
        this.userRepositoryCustom = userRepositoryCustom;
        this.passwordEncoder = passwordEncoder;
        this.organizationRepository = organizationRepository;
        this.userOrgViewRepository = userOrgViewRepository;
    }

    @Override
    public User authenticate(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }
    @Override
    public UserOrgView getCurrentUserInfo(UUID userId){
        return userOrgViewRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("User not found in view"));
    }
    @Override
    public User getUserById(String userId){
        UUID uuid = UUID.fromString(userId);
        return userRepository.findById(uuid)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }
    @Override
    public String getEmailByUserId(UUID userId) {
        return userRepository.findById(userId)
                .map(User::getEmail)
                .orElse("unknown");
    }
    @Override
    public User createUser(CreateUserRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        if (request.getOrganizationId() != null) {
            Organization org = organizationRepository.findById(request.getOrganizationId())
                    .orElseThrow(() -> new RuntimeException("Organization not found"));
            user.setOrganization(org);
        }

        user.setAdmin(Boolean.TRUE.equals(request.getIsAdmin()));
        return userRepository.save(user);
    }
    @Override
    public User registerUserWithOrganization(CreateUserWithOrganizationRequest request) {
        String orgName = request.getOrganizationName();

        Organization org = new Organization();
        org.setName(orgName);
        organizationRepository.save(org);


        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setOrganization(org);
        user.setAdmin(true);

        return userRepository.save(user);
    }
    @Override
    public User updateCurrentUser(String userId, UpdateUserRequest request) {
        UUID id = UUID.fromString(userId);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getUsername() != null) {
            user.setName(request.getUsername());
        }

        if (request.getNewPassword() != null && !request.getNewPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }

        return userRepository.save(user);
    }
    @Override
    public void toggleAdmin(UUID orgId, ToggleAdminRequest request){
        boolean isMember = getOrgMembersIds(orgId).contains(request.getUserId());
        if(!isMember){
            throw new NotFoundException("User was not found in your organization");
        }
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));
        user.setAdmin(request.getIsAdmin());

        userRepository.save(user);
    }
    @Override
    public List<OrgMemberResponse> getOrgMembers(UUID orgId) {
        return userRepository.findAllByOrganization_OrganizationId(orgId).stream()
                .map(user -> {
                    OrgMemberResponse dto = new OrgMemberResponse();
                    dto.setUserId(user.getUserId());
                    dto.setName(user.getName()); // может быть null
                    dto.setEmail(user.getEmail());
                    dto.setAdmin(user.isAdmin());
                    return dto;
                })
                .toList();
    }
    @Override
    public List<UUID> getOrgMembersIds(UUID orgId){
        return userRepository.findAllByOrganization_OrganizationId(orgId).stream()
                .map(User::getUserId)
                .toList();
    }
    @Override
    public void deleteUser(UUID userId){
        userRepositoryCustom.deleteUserWithCheck(userId);
    }
}
