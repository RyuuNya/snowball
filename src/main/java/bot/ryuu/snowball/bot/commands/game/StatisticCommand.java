package bot.ryuu.snowball.bot.commands.game;

import bot.ryuu.snowball.bot.commands.CommandAbstract;
import bot.ryuu.snowball.data.DataCluster;
import bot.ryuu.snowball.data.player.Player;
import bot.ryuu.snowball.event.Param;
import bot.ryuu.snowball.event.request.EventRequest;
import bot.ryuu.snowball.event.request.Request;
import bot.ryuu.snowball.game.GameAction;
import bot.ryuu.snowball.tools.register.Registration;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.Optional;

public class StatisticCommand extends CommandAbstract {
    public StatisticCommand(DataCluster cluster) {
        super(cluster);

        setCode("_stats_command");
        setCommand(
                Commands.slash("stats", "your profile statistics")
                        .setGuildOnly(true)
        );
    }

    @Override
    protected void slashInteraction(SlashCommandInteractionEvent slash) {
        super.slashInteraction(slash);

        Optional<Player> player = cluster.getPlayer(slash);

        if (player.isEmpty()) {
            player = Registration.register(slash.getUser(), slash.getGuild(), cluster);
        }

        if (player.isPresent()) {
            MessageEmbed embed = GameAction.execute(
                    EventRequest.of(
                            Request.STATS,
                            new Param("a", player.get()),
                            new Param("user", slash.getUser())
                    )
            ).valueNoOptional("stats");

            slash.deferReply(true).setEmbeds(embed).queue();
        } else
            replyError(slash);
    }
}