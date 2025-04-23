package sia.plants.service.task;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sia.plants.DTO.task.CreateTaskRequest;
import sia.plants.DTO.task.TaskPlantDTO;
import sia.plants.DTO.task.TaskResponse;
import sia.plants.model.Task;
import sia.plants.model.plant.Plant;
import sia.plants.model.user.User;
import sia.plants.model.user.UserTask;
import sia.plants.repository.TaskRepository;
import sia.plants.repository.plant.PlantRepository;
import sia.plants.repository.user.UserRepository;
import sia.plants.repository.user.UserTaskRepository;
import sia.plants.security.JwtService;

import java.util.List;
import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final PlantRepository plantRepository;
    private final UserRepository userRepository;
    private final UserTaskRepository userTaskRepository;
    private final JwtService jwtService;

    public TaskServiceImpl(TaskRepository taskRepository,
                           PlantRepository plantRepository,
                           UserRepository userRepository,
                           UserTaskRepository userTaskRepository,
                           JwtService jwtService) {
        this.taskRepository = taskRepository;
        this.plantRepository = plantRepository;
        this.userRepository = userRepository;
        this.userTaskRepository = userTaskRepository;
        this.jwtService = jwtService;
    }

    @Transactional
    @Override
    public void createTask(CreateTaskRequest request, String token) {


        UUID orgId = UUID.fromString(jwtService.extractOrganizationId(token));

        Plant plant = plantRepository.findById(request.getPlantId())
                .orElseThrow(() -> new RuntimeException("Plant not found"));

        if (!plant.getOrganization().getOrganizationId().equals(orgId)) {
            throw new RuntimeException("Plant doesn't belong to your organization");
        }

        Task task = new Task();
        task.setText(request.getText());
        task.setDueDate(request.getDueDate());
        task.setCompleted(false);
        task.setPlant(plant);

        taskRepository.save(task);

        for (UUID userId : request.getUserIds()) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            UserTask userTask = new UserTask();
            userTask.setTask(task);
            userTask.setUser(user);
            userTaskRepository.save(userTask);
        }
    }
    @Override
    public List<TaskResponse> getTasksForUser(UUID userId, UUID orgId, boolean isAdmin) {
        List<Task> tasks;
        if (isAdmin) {
            tasks = taskRepository.findTasksByOrganizationId(orgId);
        } else {
            tasks = taskRepository.findTasksByUserId(userId);
        }
        return tasks.stream().map(task -> {
            TaskPlantDTO plantDTO = new TaskPlantDTO(
                    task.getPlant().getPlantId(),
                    task.getPlant().getName(),
                    task.getPlant().getSpecies()
            );

            TaskResponse dto = new TaskResponse();
            dto.setTaskId(task.getTaskId());
            dto.setCompleted(task.getCompleted());
            dto.setDueDate(task.getDueDate());
            dto.setText(task.getText());
            dto.setPlant(plantDTO);

            return dto;
        }).toList();
    }
}

