package sia.plants.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sia.plants.DTO.plant.CreatePlantRequest;
import sia.plants.DTO.plant.PlantDetailDTO;
import sia.plants.exception.NotFoundException;
import sia.plants.model.plant.Plant;
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

    @GetMapping("/plantDetail/{id}")
    public ResponseEntity<?> getPlantDetail(
            @PathVariable Integer id,
            @CookieValue("jwt") String token
    ) {
        if (!jwtService.validateToken(token)) {
            throw new IllegalArgumentException("Invalid token");
        }

        String orgIdFromToken = jwtService.extractOrganizationId(token);
        if (orgIdFromToken == null) {
            throw new IllegalArgumentException("No organization information in token");
        }

        Plant plant = plantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Plant not found"));

        if (!plant.getOrganization().getOrganizationId().toString().equals(orgIdFromToken)) {
            throw new IllegalArgumentException("You are not allowed to access this plant");
        }

        PlantDetailDTO dto = new PlantDetailDTO();
        dto.setId(plant.getPlantId());
        dto.setName(plant.getName());
        dto.setSpecies(plant.getSpecies());

        if (plant.getOrganization() != null)
            dto.setOrganizationName(plant.getOrganization().getName());

        if (plant.getLocation() != null)
            dto.setLocationName(plant.getLocation().getName());

        if (plant.getPlantCategory() != null)
            dto.setCategoryName(plant.getPlantCategory().getName());

        if (plant.getPlantInfo() != null) {
            dto.setHumidity(plant.getPlantInfo().getHumidity());
            dto.setLightRequirements(plant.getPlantInfo().getLightRequirements());
            dto.setTemperatureRange(plant.getPlantInfo().getTemperatureRange());
            dto.setWater(plant.getPlantInfo().getWater());
        }

        List<String> urls = imageRepository.findAllByPlant_PlantId(plant.getPlantId())
                .stream()
                .map(Image::getUrl)
                .toList();

        dto.setImageUrls(urls);

        return ResponseEntity.ok(dto);
    }
    @PostMapping("/plant")
    public ResponseEntity<String> createSmartPlant(@RequestBody CreatePlantRequest request,
                                                   @CookieValue("jwt") String token) {
        if (!jwtService.validateToken(token)) {
            throw new IllegalArgumentException("Invalid token");
        }

        Boolean isAdmin = jwtService.extractIsAdmin(token);
        if (!Boolean.TRUE.equals(isAdmin)) {
            throw new IllegalArgumentException("Only admins can create flowers");
        }
        String orgId = jwtService.extractOrganizationId(token);
        if (orgId == null){
            throw new IllegalArgumentException("Organization of yours is not found");
        }
        request.setOrganizationId(UUID.fromString(orgId));


        try {
            plantService.createSmartPlantWithImages(request);
            return ResponseEntity.ok(Map.of("message", "Plant created successfully").toString());
        } catch (Exception e) {

            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
