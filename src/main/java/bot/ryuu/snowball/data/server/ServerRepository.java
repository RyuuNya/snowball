package bot.ryuu.snowball.data.server;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServerRepository extends JpaRepository<Server, String> {
    Optional<Server> findById(String id);
}
