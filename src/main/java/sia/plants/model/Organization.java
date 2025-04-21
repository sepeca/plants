package sia.plants.model;
import jakarta.persistence.*;
import java.util.*;
@Entity
@Table(name = "organization")
public class Organization {
    @Id
    @Column(name = "organizationid")
    private UUID organizationId;

    @Column(nullable = false)
    private String name;

    public UUID getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(UUID organizationId) {
        this.organizationId = organizationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}