package sia.plants.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sia.plants.model.CareType;



import org.springframework.stereotype.Repository;


@Repository
public interface CareTypeRepository extends JpaRepository<CareType, Integer> {
}
