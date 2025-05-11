package sia.plants.service.task;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sia.plants.DTO.task.*;
import sia.plants.entities.UserTaskView;
import sia.plants.model.Task;
import sia.plants.model.plant.Plant;
import sia.plants.model.user.User;
import sia.plants.model.user.UserTask;
import sia.plants.model.user.UserTaskId;
import sia.plants.repository.TaskRepository;
import sia.plants.repository.entities.UserTaskViewRepository;
import sia.plants.repository.plant.PlantRepository;
import sia.plants.repository.user.UserRepository;
import sia.plants.repository.user.UserTaskRepository;
import sia.plants.security.JwtService;
import sia.plants.service.user.UserService;

import java.sql.Timestamp;
import java.util.*;

@Service
public class TaskServiceImpl implements TaskService {
    @PersistenceContext
    private EntityManager entityManager;

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
    public void createTask(CreateTaskRequest request,String token) {


        UUID orgId = UUID.fromString(jwtService.extractOrganizationId(token));
        UUID currentUserId = UUID.fromString(jwtService.extractUserId(token));

        Plant plant = plantRepository.findById(request.getPlantId())
                .orElseThrow(() -> new RuntimeException("Plant not found"));

        if (!plant.getOrganization().getOrganizationId().equals(orgId)) {
            throw new RuntimeException("Plant doesn't belong to your organization");
        }

        Task task = new Task();
        task.setText(request.getDescription());
        int days = request.getFinishDate();
        task.setDueDate(
                new Timestamp(System.currentTimeMillis() + 86400000L * days)
        );
        task.setCompleted(false);
        task.setPlant(plant);

        taskRepository.save(task);

        Set<UUID> allUserIds = new HashSet<>();
        if(request.getEmails() != null){
        for(String email : request.getEmails()){
            allUserIds.add(userRepository.findByEmail(email).get().getUserId());
        }}




        if (Boolean.TRUE.equals(request.getMe())) {
            allUserIds.add(currentUserId);
        }

        for (UUID userId : allUserIds) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            UserTask userTask = new UserTask();
            userTask.setTask(task);
            userTask.setUser(user);
            userTask.setNotified(false);
            userTaskRepository.save(userTask);
        }
    }

    private TaskWithUsersDTO mapUserTaskViewToDto(UserTaskView task, String username) {
        TaskWithUsersDTO dto = new TaskWithUsersDTO();
        dto.setTaskId(task.getTaskId());
        dto.setTaskDescription(task.getTaskDescription());
        dto.setCompleted(task.getCompleted());
        dto.setDueDate(task.getDueDate());
        dto.setCareTaker(task.getCareTaker());
        dto.setPlantId(task.getPlantId());
        dto.setPlantName(task.getPlantName());
        dto.setPlantSpecies(task.getPlantSpecies());
        dto.setAssignedUsers(new ArrayList<>());
        dto.setLocationName(task.getLocationName());
        dto.setNotified(task.isNotified());

        return dto;
    }
    private TaskWithUsersDTO createTaskWithUsersDTO(Task task){
        TaskWithUsersDTO dto = new TaskWithUsersDTO();
        dto.setTaskId(task.getTaskId());
        dto.setTaskDescription(task.getText());
        dto.setCompleted(task.getCompleted());
        dto.setDueDate(task.getDueDate());
        dto.setCareTaker(task.getCareTaker());

        Plant plant = task.getPlant();
        dto.setPlantId(plant.getPlantId());
        dto.setPlantName(plant.getName());
        dto.setPlantSpecies(plant.getSpecies());
        dto.setNotified(true);
        dto.setLocationName(plant.getLocation().getName());
        return dto;
    }

    @Override
    public List<TaskWithUsersDTO> getAllTasks(UUID orgId) {
        Set<UUID> userIds = new HashSet<>(userService.getOrgMembersIds(orgId));
        Map<Integer, TaskWithUsersDTO> taskMap = new LinkedHashMap<>();


        for (UUID userId : userIds) {
            for (UserTaskView task : userTaskViewRepository.findAllByUserId(userId)) {
                String username = (task.getUsername() != null && !task.getUsername().isBlank())
                        ? task.getUsername()
                        : userService.getEmailByUserId(task.getUserId());

                taskMap.computeIfAbsent(task.getTaskId(), id -> mapUserTaskViewToDto(task, username))
                        .getAssignedUsers().add(username);
            }
        }


        List<Task> finishedTasks = new ArrayList<>();
        for (UUID userId : userIds) {
            String name = userService.getUserById(userId.toString()).getName();
            String email = userService.getEmailByUserId(userId);
            if (name != null && !name.isBlank()) {
                finishedTasks.addAll(taskRepository.findTasksByCareTakerAndOrganizationId(name, orgId));
            }
            finishedTasks.addAll(taskRepository.findTasksByCareTakerAndOrganizationId(email, orgId));
        }
        for (Task task : finishedTasks) {
            TaskWithUsersDTO dto = createTaskWithUsersDTO(task);
            dto.setAssignedUsers(Collections.singletonList(task.getCareTaker()));
            taskMap.putIfAbsent(task.getTaskId(), dto);
        }

        return new ArrayList<>(taskMap.values());
    }

    @Override
    public List<TaskWithUsersDTO> getMyTasks(UUID userId, UUID orgId) {
        Map<Integer, TaskWithUsersDTO> taskMap = new LinkedHashMap<>();


        for (UserTaskView task : userTaskViewRepository.findAllByUserId(userId)) {
            String username = (task.getUsername() != null && !task.getUsername().isBlank())
                    ? task.getUsername()
                    : userService.getEmailByUserId(task.getUserId());

            taskMap.computeIfAbsent(task.getTaskId(), id -> mapUserTaskViewToDto(task, username));
        }


        String name = userService.getUserById(userId.toString()).getName();
        String email = userService.getEmailByUserId(userId);
        List<Task> finishedTasks = new ArrayList<>();
        if (name != null && !name.isBlank()) {
            finishedTasks.addAll(taskRepository.findTasksByCareTakerAndOrganizationId(name, orgId));
        }
        finishedTasks.addAll(taskRepository.findTasksByCareTakerAndOrganizationId(email, orgId));

        for (Task task : finishedTasks) {
            TaskWithUsersDTO dto = createTaskWithUsersDTO(task);
            dto.setAssignedUsers(Collections.singletonList(task.getCareTaker()));
            taskMap.putIfAbsent(task.getTaskId(), dto);
        }

        return new ArrayList<>(taskMap.values());
    }




    @Override
    public void finishTasksNotAdmin(UUID userId, UUID orgId, FinishTasksRequest request){
        List<Integer> taskIds = request.getTaskIds();

        List<UserTaskView> userTasksCheck = userTaskViewRepository.findAllByUserIdAndTaskIdIn(userId, taskIds);
        if (userTasksCheck.size() != taskIds.size()) {
            throw new IllegalArgumentException("Some tasks do not belong to the user");
        }
        List<Task> tasks = taskRepository.findTasksByUserId(userId);


        tasks.forEach(task -> task.setCompleted(true));
        taskRepository.saveAll(tasks);

    }
    @Override
    public void finishTasksAdmin(UUID userId, UUID orgId, FinishTasksRequest request){
        List<Integer> taskIds = request.getTaskIds();

        List<Task> tasks = taskRepository.findAllById(taskIds);

        boolean allInOrg = tasks.stream()
                .allMatch(task -> task.getPlant().getOrganization().getOrganizationId().equals(orgId));

        if (!allInOrg) {
            throw new IllegalArgumentException("Some tasks do not belong to your organization");
        }

        tasks.forEach(task -> task.setCompleted(true));
        taskRepository.saveAll(tasks);
    }
    @Override
    @Transactional
    public void notifyTasks(UUID userId, List<Integer> taskIds) {

        List<UserTaskId> userTaskIds = taskIds.stream()
                .map(taskId -> {
                    UserTaskId id = new UserTaskId();
                    id.task = taskId;
                    id.user = userId;
                    return id;
                })
                .toList();
        List<UserTask> userTasks = userTaskRepository.findAllById(userTaskIds);
        for (UserTask ut : userTasks) {
            ut.setNotified(true);
        }
        userTaskRepository.saveAll(userTasks);

    }
}

