package sia.plants.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sia.plants.DTO.ApiResponse;
import sia.plants.DTO.plant.CreatePlantRequest;
import sia.plants.DTO.plant.PlantDetailDTO;
import sia.plants.DTO.plant.PlantDetailRequest;
import sia.plants.DTO.task.DeletePlantRequest;
import sia.plants.exception.NotFoundException;
import sia.plants.model.plant.Plant;
import sia.plants.model.plant.PlantInfo;
import sia.plants.repository.plant.PlantRepository;
import sia.plants.repository.plant.ImageRepository;
import sia.plants.model.plant.Image;
import sia.plants.security.JwtService;
import sia.plants.service.plant.PlantService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class PlantController {

    private final PlantRepository plantRepository;
    private final ImageRepository imageRepository;
    private final JwtService jwtService;
    private final PlantService plantService;

    public PlantController(PlantRepository plantRepository,
                           ImageRepository imageRepository,
                           JwtService jwtService,
                           PlantService plantService) {
        this.plantRepository = plantRepository;
        this.imageRepository = imageRepository;
        this.jwtService = jwtService;
        this.plantService = plantService;
    }
    @GetMapping("/get_plants")
    public ResponseEntity<?> getPlants(@RequestHeader("Authorization") String authHeader){
        String token = jwtService.extractToken(authHeader);
        String orgIdFromToken = jwtService.extractOrganizationId(token);
        UUID organizationId = UUID.fromString(orgIdFromToken);

        return ResponseEntity.ok(plantService.getAllPlantByOrgId(organizationId));
    }

    @GetMapping("/plant_detail/{id}")
    public ResponseEntity<?> getPlantDetail(
            @PathVariable Integer id,
            @RequestHeader("Authorization") String authHeader) {
        String token = jwtService.extractToken(authHeader);

        String orgIdFromToken = jwtService.extractOrganizationId(token);

        Plant plant = plantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Plant not found"));

        if (!plant.getOrganization().getOrganizationId().toString().equals(orgIdFromToken)) {
            throw new IllegalArgumentException("You are not allowed to access this plant");
        }



        return ResponseEntity.ok(plantService.getPlantDetail(plant.getPlantId()));
    }
    @PostMapping("/add_plant")
    public ResponseEntity<String> createSmartPlant(@RequestBody CreatePlantRequest request,
                                                   @RequestHeader("Authorization") String authHeader) {
        String token = jwtService.extractToken(authHeader);

        Boolean isAdmin = jwtService.extractIsAdmin(token);
        if (!Boolean.TRUE.equals(isAdmin)) {
            throw new IllegalArgumentException("Only admins can create flowers");
        }
        String orgId = jwtService.extractOrganizationId(token);

        request.setOrganizationId(UUID.fromString(orgId));


        try {
            plantService.createSmartPlantWithImages(request);
            return ResponseEntity.ok(Map.of("message", "Plant created successfully").toString());
        } catch (Exception e) {

            throw new RuntimeException(e.getMessage(), e);
        }
    }
    @DeleteMapping("/delete_plant")
    public ResponseEntity<?> deletePlant(
                                         @RequestBody DeletePlantRequest request,
                                         @RequestHeader("Authorization") String authHeader) {
            String token = jwtService.extractToken(authHeader);
        Boolean isAdmin = jwtService.extractIsAdmin(token);
        if (!Boolean.TRUE.equals(isAdmin)) {
            throw new IllegalArgumentException("Only admins can delete flowers");
        }

            plantService.deletePlant(request.getPlantId());

            return ResponseEntity.ok(ApiResponse.success());

    }

}
