package sia.plants.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import sia.plants.model.user.User;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
}
