package bot.ryuu.snowball.bot;

import bot.ryuu.snowball.bot.command.AbstractCommand;
import bot.ryuu.snowball.bot.command.game.*;
import bot.ryuu.snowball.bot.listener.GameCommandListener;
import bot.ryuu.snowball.player.Player;
import bot.ryuu.snowball.player.PlayerRepository;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class Bot {
    private static final String token = "MTE0NzU0OTE4OTM0MTk4NjgzNg.Gsqxk6.xMVVTwy3HPHRWF5bCcF5Ryd9oMnb88xXov239E";

    private final PlayerRepository playerRepository;

    private final List<AbstractCommand> commands;

    public Bot(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;

        this.commands = List.of(
            new TakeCommand(playerRepository),
            new ThrowCommand(playerRepository),
            new StatisticCommand(playerRepository),
            new RandomCommand(playerRepository),
            new RatingCommand(playerRepository),
            new KindCommand(playerRepository)
        );
    }

    public JDA getClient() throws InterruptedException {
        return JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .build().awaitReady();
    }

    @Bean
    private void build() throws InterruptedException {
        JDABuilder.createDefault(token)
                .setStatus(OnlineStatus.ONLINE)
                .addEventListeners(new GameCommandListener(commands))
                .setActivity(Activity.playing("snowball"))
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .build().awaitReady();
    }

    @Bean
    private void loadCommand() throws InterruptedException {
        ArrayList<CommandData> commandsData = new ArrayList<>();

        commands.forEach(command -> commandsData.add(command.getCommandData()));
        getClient().updateCommands().addCommands(commandsData).queue();
    }

    @Bean
    private void loadMember() throws InterruptedException {
        System.out.println("[ -- START MEMBER LOAD -- ]");
        getClient().getGuilds().forEach(guild -> {
            ArrayList<Player> players = new ArrayList<>();
            guild.loadMembers(member -> {
                players.add(Player.builder()
                        .id(member.getId())
                        .idMember(member.getId())
                        .level(0)
                        .score(0)
                        .server(guild.getId())
                        .objectPowerSet(new HashSet<>())
                        .snowballAmount(0)
                        .lastRandomObjectPower(LocalDateTime.now().minusMinutes(10))
                        .lastTakeSnowball(LocalDateTime.now().minusMinutes(10))
                        .build());
            }).onSuccess(unused -> playerRepository.saveAll(players));
        });
        System.out.println("[ -- FINISH MEMBER LOAD --] ");
    }
}
