package bot.ryuu.snowball.bot.commands.game;

import bot.ryuu.snowball.bot.commands.CommandAbstract;
import bot.ryuu.snowball.data.DataCluster;
import bot.ryuu.snowball.data.player.Player;
import bot.ryuu.snowball.event.Param;
import bot.ryuu.snowball.event.request.EventRequest;
import bot.ryuu.snowball.event.request.Request;
import bot.ryuu.snowball.game.GameAction;
import bot.ryuu.snowball.game.Time;
import bot.ryuu.snowball.game.power.Power;
import bot.ryuu.snowball.tools.Theme;
import bot.ryuu.snowball.tools.language.Messages;
import bot.ryuu.snowball.tools.language.ThemeEmoji;
import bot.ryuu.snowball.tools.register.Registration;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.Optional;

public class RandomCommand extends CommandAbstract {
    public RandomCommand(DataCluster cluster) {
        super(cluster);

        setCode("_random_command");
        setCommand(
                Commands.slash("random", "random object of power")
                        .addOptions(
                                getPowerOption(false)
                        )
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
            Power power = GameAction.execute(
                    EventRequest.of(
                            Request.RANDOM,
                            new Param("a", player.get()),
                            new Param("cluster", cluster)
                    )
            ).valueNoOptional("power");

            if (power != null) {
                slash.deferReply().setEmbeds(
                        power.give(slash.getUser().getId(), lang(slash))
                ).queue();
            } else
                slash.deferReply(true).setEmbeds(
                        Theme.main()
                                .setDescription(
                                        ThemeEmoji.TIMER.getEmoji().getAsMention() + " " +
                                                Messages.message("TIME_OVER_RANDOM_1", lang(slash)) +
                                                Time.TIMESTAMP_RANDOM_POWER +
                                                Messages.message("TIME_OVER_RANDOM_2", lang(slash)))
                                .build()
                ).queue();
        }
    }
}