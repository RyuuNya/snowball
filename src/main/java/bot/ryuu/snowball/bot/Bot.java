package bot.ryuu.snowball.bot;

import bot.ryuu.snowball.SnowballApplication;
import bot.ryuu.snowball.bot.commands.AbstractCommand;
import bot.ryuu.snowball.bot.commands.game.*;
import bot.ryuu.snowball.bot.commands.system.HelpCommand;
import bot.ryuu.snowball.bot.commands.system.LanguageCommand;
import bot.ryuu.snowball.bot.commands.system.ResetCommand;
import bot.ryuu.snowball.bot.listener.GameCommandListener;
import bot.ryuu.snowball.bot.listener.SystemListener;
import bot.ryuu.snowball.data.DataCluster;
import bot.ryuu.snowball.data.player.Player;
import bot.ryuu.snowball.data.server.Server;
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
    private final String TOKEN = SnowballApplication.getToken();

    private final DataCluster dataCluster;

    private final List<AbstractCommand> commands;

    public Bot(DataCluster dataCluster) {
        this.dataCluster = dataCluster;

        this.commands = List.of(
                new TakeCommand(dataCluster),
                new ThrowCommand(dataCluster),
                new StatisticCommand(dataCluster),
                new PowersCommand(dataCluster),
                new RandomCommand(dataCluster),
                new RatingCommand(dataCluster),

                new HelpCommand(dataCluster),
                new LanguageCommand(dataCluster),
                new ResetCommand(dataCluster)
        );
    }

    public JDA getClient() throws InterruptedException {
        return JDABuilder.createDefault(TOKEN)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .build().awaitReady();
    }

    @Bean
    private void build() throws InterruptedException {
        JDABuilder.createDefault(TOKEN)
                .setStatus(OnlineStatus.ONLINE)
                .addEventListeners(new GameCommandListener(commands))
                .addEventListeners(new SystemListener(dataCluster))
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
    private void loadMemberServer() throws InterruptedException {
        System.out.println("[ -- START MEMBER & SERVER LOAD -- ]");
        getClient().getGuilds().forEach(guild -> {
            Server server = Server.builder()
                    .id(guild.getId())
                    .language("en")
                    .build();

            ArrayList<Player> players = new ArrayList<>();

            guild.loadMembers(member -> {
                players.add(
                        Player.builder()
                                .member(member.getId())
                                .server(guild.getId())
                                .powers(new HashSet<>())
                                .snowball(0)
                                .lastRandomPower(LocalDateTime.now().minusMinutes(10))
                                .lastTakeSnowball(LocalDateTime.now().minusMinutes(10))
                                .score(0)
                                .build()
                );
            }).onSuccess(unused -> {
                dataCluster.savePlayerAll(players);
                server.save(dataCluster.getServerRepository());
            });
        });
        System.out.println("[ -- FINISH MEMBER & SERVER LOAD --] ");
    }
}
