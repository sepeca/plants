package sia.plants.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sia.plants.DTO.task.CreateTaskRequest;
import sia.plants.entities.UserTaskView;
import sia.plants.security.JwtService;
import sia.plants.service.task.TaskService;

import java.util.List;
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
    @PostMapping("/create_task")
    public ResponseEntity<?> createTask(@RequestBody CreateTaskRequest request, @CookieValue("jwt") String token) {
        if (!jwtService.validateToken(token)) {
            throw new RuntimeException("Invalid token");
        }

        boolean isAdmin = Boolean.TRUE.equals(jwtService.extractIsAdmin(token));
        if (!isAdmin) throw new RuntimeException("Only admin can create tasks");

        taskService.createTask(request, token);
        return ResponseEntity.ok("Task created");
    }
    @GetMapping("/all_tasks")
    public ResponseEntity<?> getTasks(@CookieValue("jwt") String token) {
        if (!jwtService.validateToken(token)) {
            return ResponseEntity.status(401).body("Invalid token");
        }

        boolean isAdmin = Boolean.TRUE.equals(jwtService.extractIsAdmin(token));
        if (!isAdmin) {
            throw new IllegalArgumentException("Only admins can access all user tasks.");
        }
        UUID orgId = UUID.fromString(jwtService.extractOrganizationId(token));
        List<UserTaskView> tasks = taskService.getAllTasks(orgId);

        return ResponseEntity.ok(tasks);
    }
    @GetMapping("/my_tasks")
    public ResponseEntity<?> getTasksView(@CookieValue("jwt") String token) {
        if (!jwtService.validateToken(token)) {
            throw new IllegalArgumentException("Invalid token");
        }

        UUID userId = UUID.fromString(jwtService.extractUserId(token));
        List<UserTaskView> tasks = taskService.getMyTasks(userId);
        return ResponseEntity.ok(tasks);
    }

}
