package sia.plants.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sia.plants.DTO.user.*;
import sia.plants.entities.UserOrgView;
import sia.plants.model.Organization;
import sia.plants.model.user.User;
import sia.plants.repository.OrganizationRepository;
import sia.plants.repository.user.UserRepository;
import sia.plants.service.user.UserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public ResponseEntity<?> login(
            @RequestParam String email,
            @RequestParam String password,
            HttpServletResponse response
    ){
        User user = userService.authenticate(email, password);
        String token = jwtService.generateToken(user);

        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("Lax")
                .maxAge(86400)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok("Logged in");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("Lax")
                .maxAge(0) // Удаляем cookie
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok("Logged out");
    }

    @PostMapping("/register_self")
    public ResponseEntity<?> registerUser(@RequestBody CreateUserWithOrganizationRequest request) {
            User newUser = userService.registerUserWithOrganization(request);
            String token = jwtService.generateToken(newUser);

            ResponseCookie cookie = ResponseCookie.from("jwt", token)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .sameSite("Lax")
                    .maxAge(86400)
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body("Registered and logged in successfully");

    }
    @PostMapping("/register_worker")
    public ResponseEntity<?> createUser(
            @RequestBody CreateUserRequest request,
            @CookieValue("jwt") String token
    ) {
        if (!jwtService.validateToken(token)) {
            throw new IllegalArgumentException("Invalid token");
        }

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
            @CookieValue("jwt") String token
    ) {
        if (!jwtService.validateToken(token)) {
            throw new IllegalArgumentException("Invalid token");
        }

        UUID userId = UUID.fromString(jwtService.extractUserId(token));
        UserOrgView info = userService.getCurrentUserInfo(userId);

        return ResponseEntity.ok(info);
    }
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(
            @RequestBody UpdateUserRequest request,
            @CookieValue("jwt") String token
    ) {
        if (!jwtService.validateToken(token)) {
            throw new IllegalArgumentException("Invalid token");
        }

        String userId = jwtService.extractUserId(token);
        userService.updateCurrentUser(userId, request);
        return ResponseEntity.ok("User updated successfully");
    }
    @GetMapping("/org_members")
    public ResponseEntity<?> getOrgMembers(@CookieValue("jwt") String token) {
        if (!jwtService.validateToken(token)) {
            throw new IllegalArgumentException("Invalid token");
        }

        String orgIdStr = jwtService.extractOrganizationId(token);
        if (orgIdStr == null) {
            return ResponseEntity.status(400).body("Organization ID not found in token");
        }

        UUID orgId = UUID.fromString(orgIdStr);
        List<OrgMemberResponse> members = userService.getOrgMembers(orgId);
        return ResponseEntity.ok(members);
    }
}
