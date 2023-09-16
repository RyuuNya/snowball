package bot.ryuu.snowball.bot.commands.system;

import bot.ryuu.snowball.bot.commands.AbstractCommand;
import bot.ryuu.snowball.data.DataCluster;
import bot.ryuu.snowball.data.player.Player;
import bot.ryuu.snowball.language.Language;
import bot.ryuu.snowball.theme.Theme;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.HashSet;
import java.util.List;

public class ResetCommand extends AbstractCommand {
    public ResetCommand(DataCluster dataCluster) {
        super(dataCluster);

        setCode("_reset_command");
        setCommandData(
                Commands.slash("reset", "resets all users")
                    .setGuildOnly(true)
        );
    }

    @Override
    protected void slashInteraction(SlashCommandInteractionEvent slash) {
        super.slashInteraction(slash);

        List<Player> players = dataCluster.getPlayersServer(slash);

        players.forEach(player -> {
            player.setScore(0);
            player.setActive(null);
            player.setPowers(new HashSet<>());
            player.setSnowball(0);
        });

        dataCluster.savePlayerAll(players);

        slash.deferReply(true).setEmbeds(
                Theme.getMainEmbed()
                        .setDescription(
                                Language.message("server-reset", getLanguage(slash))
                        ).build()
        ).queue();
    }
}
