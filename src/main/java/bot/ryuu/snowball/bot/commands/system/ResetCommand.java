package bot.ryuu.snowball.bot.commands.system;

import bot.ryuu.snowball.bot.commands.CommandAbstract;
import bot.ryuu.snowball.data.DataCluster;
import bot.ryuu.snowball.data.player.Player;
import bot.ryuu.snowball.tools.Theme;
import bot.ryuu.snowball.tools.language.Messages;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.HashSet;
import java.util.List;

public class ResetCommand extends CommandAbstract {
    public ResetCommand(DataCluster cluster) {
        super(cluster);

        setCode("_reset_command");
        setCommand(
                Commands.slash("reset", "resets all users")
                        .setGuildOnly(true)
        );
    }

    @Override
    protected void slashInteraction(SlashCommandInteractionEvent slash) {
        super.slashInteraction(slash);

        List<Player> players = cluster.getPlayers(slash.getGuild().getId());

        players.forEach(player -> {
            player.setScore(0);
            player.setActive(null);
            player.setPowers(new HashSet<>());
            player.setSnowball(0);
        });

        cluster.savePlayers(players);

        slash.deferReply(true).setEmbeds(
                Theme.main()
                        .setDescription(
                                Messages.message("SERVER_RESET", lang(slash))
                        ).build()
        ).queue();
    }
}