package sia.plants.entities;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "user_org_view")
public class UserOrgView {

    @Id
    @Column(name = "userid")
    private UUID userId;

    @Column(name = "username")
    private String name;

    @Column(name = "useremail")
    private String email;

    @Column(name = "organizationid")
    private UUID organizationId;

    @Column(name = "organizationname")
    private String organizationName;

    // Геттеры и сеттеры

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UUID getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(UUID organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }
}
