package sia.plants.repository.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class UserRepositoryCustom {

    private final JdbcTemplate jdbcTemplate;

    public UserRepositoryCustom(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void deleteUserWithCheck(UUID userId) {
        jdbcTemplate.update("CALL public.delete_user_with_check(?)", userId);
    }
}
