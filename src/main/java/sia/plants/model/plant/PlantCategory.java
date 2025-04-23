package sia.plants.model.plant;
import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "plant_category")
public class PlantCategory {
    @Id
    @Column(name = "plant_categoryid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer plantCategoryId;
    @Column(nullable = false)
    private String name;

    public Integer getPlantCategoryId() {
        return plantCategoryId;
    }

    public void setPlantCategoryId(Integer plantCategoryId) {
        this.plantCategoryId = plantCategoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}