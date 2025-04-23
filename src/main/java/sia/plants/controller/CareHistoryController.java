package sia.plants.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sia.plants.DTO.careHistory.CareHistoryDTO;
import sia.plants.model.CareHistory;
import sia.plants.model.CareType;
import sia.plants.model.plant.Plant;
import sia.plants.repository.CareHistoryRepository;
import sia.plants.repository.CareTypeRepository;
import sia.plants.repository.plant.PlantRepository;
import sia.plants.security.JwtService;
import sia.plants.service.care.CareService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class CareHistoryController {
    private final PlantRepository plantRepository;
    private final CareTypeRepository careTypeRepository;
    private final JwtService jwtService;
    private final CareService careService;

    public CareHistoryController(PlantRepository plantRepository,
                                 CareTypeRepository careTypeRepository,
                                 JwtService jwtService,
                                 CareService careService) {
        this.plantRepository = plantRepository;
        this.jwtService = jwtService;
        this.careService = careService;
        this.careTypeRepository = careTypeRepository;
    }
    @GetMapping("/careTypes")
    public ResponseEntity<?> getAllCareTypes(){
        List<CareType> careTypes = careTypeRepository.findAll();
        return ResponseEntity.ok(careTypes);
    }
    @PostMapping("/create_care_history}")
    public void createCareHistory(@CookieValue("jwt") String token){

    }
    @GetMapping("/careHistoryByPlant/{id}")
    public ResponseEntity<?> getPlantDetail(
            @PathVariable Integer id,
            @CookieValue("jwt") String token
    ){
        UUID orgId = UUID.fromString(jwtService.extractOrganizationId(token));

        Plant plant = plantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plant not found"));

        if (!plant.getOrganization().getOrganizationId().equals(orgId)) {
            throw new RuntimeException("Access denied: Plant does not belong to your organization");
        }
        List<CareHistoryDTO> dto = careService.getCareHistoryByPlantId(id);
        return ResponseEntity.ok(dto);
    }
    @GetMapping("/careHistoryMy")
    public ResponseEntity<?> getPlantDetail(

            @CookieValue("jwt") String token
    ){
        if (!jwtService.validateToken(token)) {
            throw new IllegalArgumentException("Invalid token");
        }
        UUID uuid = UUID.fromString(jwtService.extractUserId(token));
        List<CareHistoryDTO> dto = careService.getCareHistoryByUserId(uuid);
        return ResponseEntity.ok(dto);
    }

}
