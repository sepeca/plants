package sia.plants.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sia.plants.DTO.task.CreateTaskRequest;
import sia.plants.security.JwtService;
import sia.plants.service.task.TaskService;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class TaskController {
    private final TaskService taskService;
    private final JwtService jwtService;
    public TaskController(TaskService taskService,
                          JwtService jwtService){
        this.taskService = taskService;
        this.jwtService = jwtService;
    }
    @PostMapping("/task")
    public ResponseEntity<?> createTask(@RequestBody CreateTaskRequest request, @CookieValue("jwt") String token) {
        if (!jwtService.validateToken(token)) {
            throw new RuntimeException("Invalid token");
        }

        boolean isAdmin = Boolean.TRUE.equals(jwtService.extractIsAdmin(token));
        if (!isAdmin) throw new RuntimeException("Only admin can create tasks");

        taskService.createTask(request, token);
        return ResponseEntity.ok("Task created");
    }
    @GetMapping("/task")
    public ResponseEntity<?> getTasks(@CookieValue("jwt") String token) {
        if (!jwtService.validateToken(token)) {
            return ResponseEntity.status(401).body("Invalid token");
        }

        UUID userId = UUID.fromString(jwtService.extractUserId(token));
        boolean isAdmin = Boolean.TRUE.equals(jwtService.extractIsAdmin(token));
        UUID orgId = UUID.fromString(jwtService.extractOrganizationId(token));

        return ResponseEntity.ok(taskService.getTasksForUser(userId, orgId, isAdmin));
    }
}
