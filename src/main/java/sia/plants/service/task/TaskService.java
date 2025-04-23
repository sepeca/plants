package sia.plants.service.task;

import sia.plants.DTO.task.CreateTaskRequest;
import sia.plants.DTO.task.TaskResponse;
import sia.plants.model.Task;

import java.util.List;
import java.util.UUID;

public interface TaskService {
    void createTask(CreateTaskRequest request, String token);
    List<TaskResponse> getTasksForUser(UUID userId, UUID orgId, boolean isAdmin);
}