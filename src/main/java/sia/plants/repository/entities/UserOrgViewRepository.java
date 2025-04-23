package sia.plants.repository.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import sia.plants.entities.UserOrgView;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserOrgViewRepository extends JpaRepository<UserOrgView, UUID> {
    List<UserOrgView> findByOrganizationId(UUID organizationId);
    Optional<UserOrgView> findByUserId(UUID userId);
}
