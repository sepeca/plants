package sia.plants.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sia.plants.DTO.ApiResponse;
import sia.plants.DTO.user.*;
import sia.plants.entities.UserOrgView;
import sia.plants.model.Organization;
import sia.plants.model.user.User;
import sia.plants.repository.OrganizationRepository;
import sia.plants.repository.user.UserRepository;
import sia.plants.service.user.UserService;

import java.util.*;

@RestController
public class AuthController {

    private final JwtService jwtService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    public AuthController(JwtService jwtService, UserService userService,
                          OrganizationRepository organizationRepository,
                          UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.organizationRepository = organizationRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();
        User user = userService.authenticate(email, password);
        String token = jwtService.generateToken(user);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(HttpServletResponse response,
                                              @RequestHeader("Authorization") String authHeader) {
        String token = jwtService.extractToken(authHeader);
        jwtService.blacklistToken(token);
        return  ResponseEntity.ok(ApiResponse.success());
    }

    @PostMapping("/register_self")
    public ResponseEntity<?> registerUser(@RequestBody CreateUserWithOrganizationRequest request) {
        userService.registerUserWithOrganization(request);


        return ResponseEntity.ok(ApiResponse.success());

    }
    @PostMapping("/register_worker")
    public ResponseEntity<?> createUser(
            @RequestBody CreateUserRequest request,
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.substring(7);
        jwtService.validate(token);

        Boolean isAdmin = jwtService.extractIsAdmin(token);
        if (!Boolean.TRUE.equals(isAdmin)) {
            throw new IllegalArgumentException("Only admins can create users");
        }

        if (request.getOrganizationId() == null) {
            String orgId = jwtService.extractOrganizationId(token);
            if (orgId != null) {
                request.setOrganizationId(UUID.fromString(orgId));
            }
        }

        userService.createUser(request);
        return ResponseEntity.ok("User created successfully");
    }
    @GetMapping("/profile")
    public ResponseEntity<?> profile(
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = jwtService.extractToken(authHeader);

        UUID userId = UUID.fromString(jwtService.extractUserId(token));
        UserOrgView info = userService.getCurrentUserInfo(userId);

        return ResponseEntity.ok(info);
    }
    @PutMapping("/profile")
    public ResponseEntity<ApiResponse> updateProfile(
            @RequestBody UpdateUserRequest request,
            @RequestHeader("Authorization") String authHeader
    ) {


        String token = jwtService.extractToken(authHeader);

        String userId = jwtService.extractUserId(token);
        userService.updateCurrentUser(userId, request);
        return ResponseEntity.ok(ApiResponse.success());
    }
    @GetMapping("api/get_users")
    public ResponseEntity<?> getOrgMembers(@RequestHeader("Authorization") String authHeader) {
        String token = jwtService.extractToken(authHeader);

        String orgIdStr = jwtService.extractOrganizationId(token);


        UUID orgId = UUID.fromString(orgIdStr);
        List<OrgMemberResponse> members = userService.getOrgMembers(orgId);
        return ResponseEntity.ok(members);
    }
    @GetMapping("api/get_organization")
    public ResponseEntity<?> getOrganizationName(@RequestHeader("Authorization") String authHeader){
        String token = jwtService.extractToken(authHeader);

        String orgIdStr = jwtService.extractOrganizationId(token);

        UUID orgId = UUID.fromString(orgIdStr);
        Organization organization = organizationRepository.findById(orgId)
                .orElseThrow(() -> new IllegalArgumentException("Organization not found"));

        Map<String, String> response = new HashMap<>();
        response.put("organizationName", organization.getName());
        return ResponseEntity.ok(response);
    }
    @PutMapping("api/toggle_admin")
    public  ResponseEntity<ApiResponse> toggleAdmin(@RequestHeader("Authorization") String authHeader,
                                                    @RequestBody ToggleAdminRequest request){
        String token = jwtService.extractToken(authHeader);
        String orgIdStr = jwtService.extractOrganizationId(token);

        UUID orgId = UUID.fromString(orgIdStr);
        userService.toggleAdmin(orgId, request);

        return  ResponseEntity.ok(ApiResponse.success());
    }
    @DeleteMapping("api/delete_users")
    public ResponseEntity<ApiResponse> deleteUser(@RequestHeader("Authorization") String authHeader,
                                                  @RequestBody DeleteUserRequest request){
        String token = jwtService.extractToken(authHeader);
        Boolean isAdmin = jwtService.extractIsAdmin(token);
        UUID selfId = UUID.fromString(jwtService.extractUserId(token));
        UUID userId = UUID.fromString(request.getUserId());

        if(!isAdmin && userId != selfId){
            throw new RuntimeException("Only Admins can delete users");
        }
        userService.deleteUser(userId);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
