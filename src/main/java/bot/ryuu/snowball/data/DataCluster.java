package bot.ryuu.snowball.data;

import bot.ryuu.snowball.data.player.Player;
import bot.ryuu.snowball.data.player.PlayerRepository;
import bot.ryuu.snowball.data.server.Server;
import bot.ryuu.snowball.data.server.ServerRepository;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class DataCluster {
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private ServerRepository serverRepository;

    @Deprecated
    public Optional<Player> getPlayer(String id) {
        return playerRepository.findById(id);
    }

    public Optional<Player> getPlayerProfile(SlashCommandInteractionEvent event) {
        return playerRepository.findByMemberAndServer(event.getMember().getId(), event.getGuild().getId());
    }

    public Optional<Player> getPlayerProfile(String member, String server) {
        return playerRepository.findByMemberAndServer(member, server);
    }

    public List<Player> getPlayersServer(String server) {
        return playerRepository.findAllByServer(server);
    }

    public List<Player> getPlayersServer(SlashCommandInteractionEvent event) {
        return playerRepository.findAllByServer(event.getGuild().getId());
    }

    public void savePlayerAll(Iterable<Player> players) {
        playerRepository.saveAll(players);
    }

    public PlayerRepository getPlayerRepository() {
        return playerRepository;
    }

    public Optional<Server> getServer(String id) {
        return serverRepository.findById(id);
    }

    public Optional<Server> getServer(SlashCommandInteractionEvent event) {
        return serverRepository.findById(event.getGuild().getId());
    }

    public void saveServerAll(Iterable<Server> servers) {
        serverRepository.saveAll(servers);
    }

    public ServerRepository getServerRepository() {
        return serverRepository;
    }
}
