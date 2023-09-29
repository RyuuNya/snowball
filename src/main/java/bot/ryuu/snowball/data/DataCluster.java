package bot.ryuu.snowball.data;

import bot.ryuu.snowball.data.player.Player;
import bot.ryuu.snowball.data.player.PlayerRepository;
import bot.ryuu.snowball.data.server.Server;
import bot.ryuu.snowball.data.server.ServerRepository;
import lombok.Getter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Getter
@Component
public class DataCluster {
    @Autowired
    private ServerRepository serverRepository;
    @Autowired
    private PlayerRepository playerRepository;

    public Optional<Player> getPlayer(String id) {
        return playerRepository.findById(id);
    }

    public Optional<Player> getPlayer(String member, String server) {
        return playerRepository.findByMemberAndServer(member, server);
    }

    public Optional<Player> getPlayer(SlashCommandInteractionEvent slash) {
        if (slash.getGuild() != null)
            return playerRepository.findByMemberAndServer(slash.getUser().getId(), slash.getGuild().getId());
        else
            return Optional.empty();
    }

    public Optional<Player> getPlayer(ButtonInteractionEvent button) {
        if (button.getGuild() != null)
            return playerRepository.findByMemberAndServer(button.getUser().getId(), button.getGuild().getId());
        else
            return Optional.empty();
    }

    public List<Player> getPlayers(String server) {
        return playerRepository.findAllByServer(server);
    }

    public void savePlayers(Player... players) {
        playerRepository.saveAll(List.of(players));
    }


    public void savePlayers(Iterable<Player> players) {
        playerRepository.saveAll(players);
    }


    public Optional<Server> getServer(String id) {
        return serverRepository.findById(id);
    }

    public Optional<Server> getServer(SlashCommandInteractionEvent slash) {
        if (slash.getGuild() != null)
            return serverRepository.findById(slash.getGuild().getId());
        else
            return Optional.empty();
    }

    public Optional<Server> getServer(ButtonInteractionEvent slash) {
        if (slash.getGuild() != null)
            return serverRepository.findById(slash.getGuild().getId());
        else
            return Optional.empty();
    }

    public void saveServers(Server... servers) {
        serverRepository.saveAll(List.of(servers));
    }

    public void saveServers(Iterable<Server> servers) {
        serverRepository.saveAll(servers);
    }
}