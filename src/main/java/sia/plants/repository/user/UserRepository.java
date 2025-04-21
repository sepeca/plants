package sia.plants.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import sia.plants.model.user.User;

import java.util.UUID;


public interface UserRepository extends JpaRepository<User, UUID> {
}
