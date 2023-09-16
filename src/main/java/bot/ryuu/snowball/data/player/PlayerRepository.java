package bot.ryuu.snowball.data.player;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, String> {
    @NotNull
    Optional<Player> findById(@NotNull String id);

    Optional<Player> findByMemberAndServer(String member, String server);

    List<Player> findAllByServer(String server);
}
