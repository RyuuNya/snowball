package bot.ryuu.snowball.tools.register;

import bot.ryuu.snowball.data.DataCluster;
import bot.ryuu.snowball.data.player.Player;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;

public interface Registration {
    static Optional<Player> register(User user, Guild guild, DataCluster cluster) {
        Player player = Player.builder()
                .id(user.getId())
                .member(user.getId())
                .server(guild.getId())
                .powers(new HashSet<>())
                .active(null)
                .score(0)
                .snowball(0)
                .lastRandomPower(LocalDateTime.now().minusMinutes(15))
                .lastTakeSnowball(LocalDateTime.now().minusMinutes(15))
                .build();

        player.save(cluster);
        return Optional.of(player);
    }
}