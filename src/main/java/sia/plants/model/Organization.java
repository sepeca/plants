package sia.plants.model;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.*;
@Entity
@Table(name = "organization")
public class Organization {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "organizationid", updatable = false, nullable = false)
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