package travel.winwin.authapi.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import travel.winwin.authapi.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);
}
