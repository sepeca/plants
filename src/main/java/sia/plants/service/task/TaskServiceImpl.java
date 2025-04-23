package sia.plants.service.task;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sia.plants.DTO.task.CreateTaskRequest;
import sia.plants.DTO.task.TaskPlantDTO;
import sia.plants.DTO.task.TaskResponse;
import sia.plants.entities.UserTaskView;
import sia.plants.model.Task;
import sia.plants.model.plant.Plant;
import sia.plants.model.user.User;
import sia.plants.model.user.UserTask;
import sia.plants.repository.TaskRepository;
import sia.plants.repository.entities.UserTaskViewRepository;
import sia.plants.repository.plant.PlantRepository;
import sia.plants.repository.user.UserRepository;
import sia.plants.repository.user.UserTaskRepository;
import sia.plants.security.JwtService;
import sia.plants.service.user.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final PlantRepository plantRepository;
    private final UserRepository userRepository;
    private final UserTaskRepository userTaskRepository;
    private final UserTaskViewRepository userTaskViewRepository;
    private final JwtService jwtService;
    private final UserService userService;

    public TaskServiceImpl(TaskRepository taskRepository,
                           PlantRepository plantRepository,
                           UserRepository userRepository,
                           UserTaskRepository userTaskRepository,
                           UserTaskViewRepository userTaskViewRepository,
                           JwtService jwtService,
                           UserService userService) {
        this.taskRepository = taskRepository;
        this.plantRepository = plantRepository;
        this.userRepository = userRepository;
        this.userTaskRepository = userTaskRepository;
        this.userTaskViewRepository = userTaskViewRepository;
        this.jwtService = jwtService;
        this.userService = userService;
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
    public List<UserTaskView> getAllTasks(UUID orgId) {
        List<UUID> userIds = userService.getOrgMembersIds(orgId);
        List<UserTaskView> allTasks = new ArrayList<>();
        for (UUID userId : userIds) {
            allTasks.addAll(getMyTasks(userId));
        }
        return allTasks;
    }
    @Override
    public List<UserTaskView> getMyTasks(UUID userId) {
        return userTaskViewRepository.findAllByUserId(userId);
    }
}

