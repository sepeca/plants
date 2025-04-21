package sia.plants.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sia.plants.model.CareType;

import java.util.UUID;


public interface CareTypeRepository extends JpaRepository<CareType, Short> {
}
