package bot.ryuu.snowball.player;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, String> {
    @NotNull
    Optional<Player> findById(@NotNull String id);

    @NotNull
    List<Player> findAllByServer(@NotNull String server);
}
