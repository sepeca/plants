package sia.plants.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sia.plants.DTO.ApiResponse;
import sia.plants.DTO.careHistory.CareHistoryDTO;
import sia.plants.DTO.careHistory.CreateCareHistoryRequest;
import sia.plants.DTO.careHistory.DeleteCareHistoryRequest;
import sia.plants.DTO.careHistory.UpdateCareHistoryRequest;
import sia.plants.exception.NotFoundException;
import sia.plants.model.CareHistory;
import sia.plants.model.CareType;
import sia.plants.model.plant.Plant;
import sia.plants.model.user.User;
import sia.plants.repository.CareHistoryRepository;
import sia.plants.repository.CareTypeRepository;
import sia.plants.repository.plant.PlantRepository;
import sia.plants.repository.user.UserRepository;
import sia.plants.security.JwtService;
import sia.plants.service.care.CareService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class CareHistoryController {
    private final PlantRepository plantRepository;
    private final CareTypeRepository careTypeRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final CareService careService;

    public CareHistoryController(PlantRepository plantRepository,
                                 CareTypeRepository careTypeRepository,
                                 UserRepository userRepository,
                                 JwtService jwtService,
                                 CareService careService) {
        this.plantRepository = plantRepository;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.careService = careService;
        this.careTypeRepository = careTypeRepository;
    }
    @GetMapping("/care_types")
    public ResponseEntity<?> getAllCareTypes(){
        List<CareType> careTypes = careTypeRepository.findAll();
        return ResponseEntity.ok(careTypes);
    }
    @PostMapping("/create_care_history")
    public ResponseEntity<?> createCareHistory(
            @RequestBody CreateCareHistoryRequest request,
            @RequestHeader("Authorization") String authHeader){
        String token = jwtService.extractToken(authHeader);
        Plant plant = plantRepository.findById(request.getPlantId())
                .orElseThrow(() -> new IllegalArgumentException("Plant not found"));
        UUID userId = request.getUserId();


        if(userId == null) {
            userId = UUID.fromString(jwtService.extractUserId(token));
        }else{
            boolean isAdmin = Boolean.TRUE.equals(jwtService.extractIsAdmin(token));
            if (!isAdmin) throw new RuntimeException("Only admin can assign event to user expect himself");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        CareType careType = careTypeRepository.findById(request.getCareTypeId())
                .orElseThrow(() -> new IllegalArgumentException("Care type not found"));

        try {
            careService.createCareHistory(request, plant, user, careType);
            return ResponseEntity.ok("Care history entry created");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }
    @PutMapping("/update_care_history/")
    public ResponseEntity<?> updateCareHistory(

            @RequestBody UpdateCareHistoryRequest request,
            @RequestHeader("Authorization") String authHeader){
        String token = jwtService.extractToken(authHeader);
        int id = request.getCareHistoryId();
        try {
            jwtService.validate(token);
            UUID currentUserId = UUID.fromString(jwtService.extractUserId(token));
            boolean isAdmin = jwtService.extractIsAdmin(token);

            CareHistory updated = careService.updateCareHistory(id, request, currentUserId, isAdmin);
            return ResponseEntity.ok(updated);
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(403).body(Map.of("error", e.getMessage()));
        }
    }
    @GetMapping("/care_history_by_plant/{id}")
    public ResponseEntity<?> getPlantDetail(
            @PathVariable Integer id,
            @RequestHeader("Authorization") String authHeader){
        String token = jwtService.extractToken(authHeader);
        UUID orgId = UUID.fromString(jwtService.extractOrganizationId(token));

        Plant plant = plantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plant not found"));

        if (!plant.getOrganization().getOrganizationId().equals(orgId)) {
            throw new RuntimeException("Access denied: Plant does not belong to your organization");
        }
        List<CareHistoryDTO> dto = careService.getCareHistoryByPlantId(id);
        return ResponseEntity.ok(dto);
    }
    @GetMapping("/care_history_my")
    public ResponseEntity<?> getPlantDetail(

            @RequestHeader("Authorization") String authHeader){
        String token = jwtService.extractToken(authHeader);
        UUID uuid = UUID.fromString(jwtService.extractUserId(token));
        List<CareHistoryDTO> dto = careService.getCareHistoryByUserId(uuid);
        return ResponseEntity.ok(dto);
    }
    @DeleteMapping("/care_history_delete")
    public ResponseEntity<?> deleteCareHistory(
            @RequestBody DeleteCareHistoryRequest request,
            @RequestHeader("Authorization") String authHeader){
        String token = jwtService.extractToken(authHeader);
        UUID deleterId = UUID.fromString(jwtService.extractUserId(token));
        return ResponseEntity.ok(ApiResponse.success());
    }
}
