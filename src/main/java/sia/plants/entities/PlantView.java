package sia.plants.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "plant_view")
public class PlantView {
    @Id
    @Column(name = "plantid")
    private Integer plantId;
    @Column(name = "plantname")
    private String plantName;
    @Column(name = "locationname")
    private String locationName;
    @Column(name = "categoryname")
    private String categoryName;
}
