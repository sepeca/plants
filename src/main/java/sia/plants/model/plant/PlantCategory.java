package sia.plants.model.plant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Entity
@Table(name = "plant_category")
public class PlantCategory {
    @Id
    @Column(name = "plant_categoryid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer plantCategoryId;

    @Setter
    @Getter
    @Column(nullable = false)
    private String name;


}