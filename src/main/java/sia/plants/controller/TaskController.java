package sia.plants.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sia.plants.DTO.ApiResponse;
import sia.plants.DTO.task.CreateTaskRequest;
import sia.plants.DTO.task.FinishTasksRequest;
import sia.plants.DTO.task.TaskWithUsersDTO;

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
    public ResponseEntity<ApiResponse> createTask(@RequestBody CreateTaskRequest request,
                                                  @RequestHeader("Authorization") String authHeader) {
        System.out.println("Auth Header: " + authHeader);
        String token = jwtService.extractToken(authHeader);

        boolean isAdmin = Boolean.TRUE.equals(jwtService.extractIsAdmin(token));
        if (!isAdmin) throw new RuntimeException("Only admin can create tasks");

        taskService.createTask(request, token);
        return ResponseEntity.ok(ApiResponse.success());
    }
    @GetMapping("/get_tasks")
    public ResponseEntity<?> getTasks(@RequestHeader("Authorization") String authHeader) {
        String token = jwtService.extractToken(authHeader);

        boolean isAdmin = Boolean.TRUE.equals(jwtService.extractIsAdmin(token));
        UUID orgId = UUID.fromString(jwtService.extractOrganizationId(token));
        if (!isAdmin) {
            UUID userId = UUID.fromString(jwtService.extractUserId(token));
            List<TaskWithUsersDTO> tasks = taskService.getMyTasks(userId, orgId);
            return ResponseEntity.ok(tasks);
        }

        List<TaskWithUsersDTO> tasks = taskService.getAllTasks(orgId);

        return ResponseEntity.ok(tasks);
    }
    @PutMapping("/finish_tasks")
    public ResponseEntity<ApiResponse> finishTasks(@RequestHeader("Authorization") String authHeader,
                                                   @RequestBody FinishTasksRequest request){
        String token = jwtService.extractToken(authHeader);
        boolean isAdmin = Boolean.TRUE.equals(jwtService.extractIsAdmin(token));
        UUID orgId = UUID.fromString(jwtService.extractOrganizationId(token));
        UUID userId = UUID.fromString(jwtService.extractUserId(token));
        if(!isAdmin){
            taskService.finishTasksNotAdmin(userId,orgId,request);
            return ResponseEntity.ok(ApiResponse.success());
        }
        taskService.finishTasksAdmin(userId,orgId,request);

        return ResponseEntity.ok(ApiResponse.success());

    }
    @PutMapping("/notify_tasks")
    public  ResponseEntity<ApiResponse> notifyTasks(@RequestHeader("Authorization") String authHeader,
                                                    @RequestBody FinishTasksRequest request
                                                     ){
        String token = jwtService.extractToken(authHeader);
        UUID userId = UUID.fromString(jwtService.extractUserId(token));
        taskService.notifyTasks(userId, request.getTaskIds());
        return ResponseEntity.ok(ApiResponse.success());
    }
}
