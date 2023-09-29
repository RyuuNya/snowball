package bot.ryuu.snowball.bot;

import bot.ryuu.snowball.SnowballApplication;
import bot.ryuu.snowball.bot.commands.CommandAbstract;
import bot.ryuu.snowball.bot.commands.game.*;
import bot.ryuu.snowball.bot.commands.system.HelpCommand;
import bot.ryuu.snowball.bot.commands.system.LanguageCommand;
import bot.ryuu.snowball.bot.commands.system.ResetCommand;
import bot.ryuu.snowball.bot.listener.GameCommandListener;
import bot.ryuu.snowball.bot.listener.SystemListener;
import bot.ryuu.snowball.data.DataCluster;
import bot.ryuu.snowball.data.player.Player;
import bot.ryuu.snowball.data.server.Server;
import bot.ryuu.snowball.tools.language.Language;
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
    private static final String TOKEN = SnowballApplication.getToken();

    private final DataCluster cluster;

    private final List<CommandAbstract> commands;

    public Bot(DataCluster cluster) {
        this.cluster = cluster;
        this.commands = List.of(
                new TakeCommand(cluster),
                new ThrowCommand(cluster),
                new StatisticCommand(cluster),
                new PowersCommand(cluster),
                new RandomCommand(cluster),

                //new GiveCommand(dataCluster),

                new HelpCommand(cluster),
                new LanguageCommand(cluster),
                new ResetCommand(cluster)
        );
    }

    @Bean
    public void build() throws InterruptedException {
        JDABuilder.createDefault(TOKEN)
                .setStatus(OnlineStatus.ONLINE)
                .addEventListeners(new GameCommandListener(commands))
                .addEventListeners(new SystemListener(cluster))
                .setActivity(Activity.playing("snowball"))
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .build().awaitReady();
    }

    @Bean
    public void loadCommand() throws InterruptedException {
        ArrayList<CommandData> commandsData = new ArrayList<>();

        commands.forEach(command -> commandsData.add(command.getCommand()));
        getClient().updateCommands().addCommands(commandsData).queue();
    }

    @Bean
    public void loadServer() throws InterruptedException {
        ArrayList<Server> servers = new ArrayList<>();
        getClient().getGuilds().forEach(guild -> {
            Server server = Server.builder()
                    .id(guild.getId())
                    .language(Language.EN)
                    .build();
            servers.add(server);
        });
        cluster.saveServers(servers);
    }

    public JDA getClient() throws InterruptedException {
        return JDABuilder.createDefault(TOKEN)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .build().awaitReady();
    }
}