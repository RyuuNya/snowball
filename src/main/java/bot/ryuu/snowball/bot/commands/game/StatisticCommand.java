package bot.ryuu.snowball.bot.commands.game;

import bot.ryuu.snowball.bot.commands.AbstractCommand;
import bot.ryuu.snowball.data.DataCluster;
import bot.ryuu.snowball.data.player.Player;
import bot.ryuu.snowball.gamev2.GameAction;
import bot.ryuu.snowball.gamev2.event.Param;
import bot.ryuu.snowball.gamev2.event.request.EventRequest;
import bot.ryuu.snowball.gamev2.event.request.Request;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.Optional;

public class StatisticCommand extends AbstractCommand {
    public StatisticCommand(DataCluster dataCluster) {
        super(dataCluster);

        setCode("_stats_command");
        setCommandData(
                Commands.slash("stats", "your profile statistics")
                    .setGuildOnly(true)
        );
    }

    @Override
    protected void slashInteraction(SlashCommandInteractionEvent slash) {
        Optional<Player> player = getPlayer(slash);

        if (player.isPresent()) {
            MessageEmbed embed = GameAction.execute(
                    EventRequest.of(
                            Request.STATS,
                            new Param("a", player.get()),
                            new Param("user", slash.getUser())
                    )
            ).value("stats");

            slash.deferReply(true).setEmbeds(embed).queue();
        } else
            replyError(slash);
    }
}
