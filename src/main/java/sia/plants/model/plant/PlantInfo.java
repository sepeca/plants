package sia.plants.model.plant;
import jakarta.persistence.*;

@Entity
@Table(name = "plant_info")
public class PlantInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer plantInfoId;

    private String humidity;
    private String lightRequirements;
    private Short water;
    private String temperatureRange;

    public Integer getPlantInfoId() {
        return plantInfoId;
    }

    public void setPlantInfoId(Integer plantInfoId) {
        this.plantInfoId = plantInfoId;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getLightRequirements() {
        return lightRequirements;
    }

    public void setLightRequirements(String lightRequirements) {
        this.lightRequirements = lightRequirements;
    }

    public Short getWater() {
        return water;
    }

    public void setWater(Short water) {
        this.water = water;
    }

    public String getTemperatureRange() {
        return temperatureRange;
    }

    public void setTemperatureRange(String temperatureRange) {
        this.temperatureRange = temperatureRange;
    }
}

