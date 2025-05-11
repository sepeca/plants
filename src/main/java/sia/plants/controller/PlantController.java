package sia.plants.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sia.plants.DTO.ApiResponse;
import sia.plants.DTO.plant.CreatePlantRequest;
import sia.plants.DTO.task.DeletePlantRequest;
import sia.plants.exception.NotFoundException;
import sia.plants.service.imageHandler.ImageHandler;
import sia.plants.model.plant.Plant;
import sia.plants.repository.plant.PlantRepository;
import sia.plants.security.JwtService;
import sia.plants.service.plant.PlantService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/api")
public class PlantController {

    private final PlantRepository plantRepository;
    private final ImageHandler imageHandler;
    private final JwtService jwtService;
    private final PlantService plantService;

    public PlantController(PlantRepository plantRepository,
                           ImageHandler imageHandler,
                           JwtService jwtService,
                           PlantService plantService) {
        this.plantRepository = plantRepository;
        this.imageHandler = imageHandler;
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
    @PostMapping(value = "/add_plant", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createSmartPlant(
            @RequestPart("plantData") CreatePlantRequest data,
            @RequestPart(value = "plantImages", required = false) List<MultipartFile> images,
            @RequestHeader("Authorization") String authHeader) throws IOException {
        String token = jwtService.extractToken(authHeader);

        Boolean isAdmin = jwtService.extractIsAdmin(token);
        if (!Boolean.TRUE.equals(isAdmin)) {
            throw new IllegalArgumentException("Only admins can create flowers");
        }
        String orgId = jwtService.extractOrganizationId(token);
        data.setOrganizationId(UUID.fromString(orgId));
        List<String> imageUrls = imageHandler.imagesToUrls(images);

        try {
            plantService.createSmartPlantWithImages(data, imageUrls);
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
