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

    public Integer getPlantId() {
        return plantId;
    }

    public void setPlantId(Integer plantId) {
        this.plantId = plantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

}