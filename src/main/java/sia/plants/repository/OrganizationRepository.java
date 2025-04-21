package sia.plants.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sia.plants.model.Organization;

import java.util.UUID;

public interface OrganizationRepository extends JpaRepository<Organization, UUID> {
}
