package bot.ryuu.snowball.bot.commands.game;

import bot.ryuu.snowball.bot.commands.AbstractCommand;
import bot.ryuu.snowball.data.DataCluster;
import bot.ryuu.snowball.data.player.Player;
import bot.ryuu.snowball.game.EventAction;
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
            slash.deferReply(true).setEmbeds(
                    EventAction.statisticPlayer(player.get(), slash.getUser())
            ).queue();
        } else
            replyError(slash);
    }
}
