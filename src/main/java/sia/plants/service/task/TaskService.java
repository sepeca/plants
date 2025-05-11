package sia.plants.service.task;

import sia.plants.DTO.task.CreateTaskRequest;
import sia.plants.DTO.task.FinishTasksRequest;
import sia.plants.DTO.task.TaskResponse;
import sia.plants.DTO.task.TaskWithUsersDTO;
import sia.plants.entities.UserTaskView;
import sia.plants.model.Task;

import java.util.List;
import java.util.UUID;

public interface TaskService {
    void createTask(CreateTaskRequest request, String token);
    List<TaskWithUsersDTO> getAllTasks(UUID orgId);
    List<TaskWithUsersDTO>  getMyTasks(UUID userId, UUID orgId);
    void finishTasksNotAdmin(UUID userId, UUID orgId, FinishTasksRequest request);
    void finishTasksAdmin(UUID userId, UUID orgId, FinishTasksRequest request);
    void notifyTasks(UUID userId, List<Integer> taskIds);
}