package sia.plants.model.plant;

import jakarta.persistence.*;
import sia.plants.model.Organization;

import java.util.*;


@Entity
@Table(name = "plant")
public class Plant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer plantId;

    private String name;
    private String species;

    @ManyToOne
    @JoinColumn(name = "organizationid")
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "locationid")
    private Location location;

    @ManyToOne
    @JoinColumn(name = "plant_infoid")
    private PlantInfo plantInfo;

    @ManyToOne
    @JoinColumn(name = "plant_categoryid")
    private PlantCategory plantCategory;

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

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public PlantInfo getPlantInfo() {
        return plantInfo;
    }

    public void setPlantInfo(PlantInfo plantInfo) {
        this.plantInfo = plantInfo;
    }

    public PlantCategory getPlantCategory() {
        return plantCategory;
    }

    public void setPlantCategory(PlantCategory plantCategory) {
        this.plantCategory = plantCategory;
    }
}

