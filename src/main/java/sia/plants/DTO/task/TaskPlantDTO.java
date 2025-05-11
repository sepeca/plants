package sia.plants.DTO.task;

import lombok.Data;

@Data
public class TaskPlantDTO {
    private Integer plantId;
    private String name;
    private String species;

    public TaskPlantDTO(Integer plantId, String name, String species) {
        this.plantId = plantId;
        this.name = name;
        this.species = species;
    }





}