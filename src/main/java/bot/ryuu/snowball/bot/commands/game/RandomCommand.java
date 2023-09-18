package bot.ryuu.snowball.bot.commands.game;

import bot.ryuu.snowball.bot.commands.AbstractCommand;
import bot.ryuu.snowball.data.DataCluster;
import bot.ryuu.snowball.data.player.Player;
import bot.ryuu.snowball.gamev2.GameAction;
import bot.ryuu.snowball.gamev2.Time;
import bot.ryuu.snowball.gamev2.event.Param;
import bot.ryuu.snowball.gamev2.event.request.EventRequest;
import bot.ryuu.snowball.gamev2.event.request.Request;
import bot.ryuu.snowball.gamev2.power.Power;
import bot.ryuu.snowball.language.Language;
import bot.ryuu.snowball.theme.Theme;
import bot.ryuu.snowball.theme.ThemeEmoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.Optional;

public class RandomCommand extends AbstractCommand {
    public RandomCommand(DataCluster dataCluster) {
        super(dataCluster);

        setCode("_random_command");
        setCommandData(
                Commands.slash("random", "random object of power")
                        .addOptions(
                                getPowerOption(false)
                        )
                        .setGuildOnly(true)
        );
    }

    @Override
    protected void slashInteraction(SlashCommandInteractionEvent slash) {
        Optional<Player> player = getPlayer(slash);

        if (player.isPresent()) {
            Power power = GameAction.execute(
                    EventRequest.of(
                            Request.RANDOM,
                            new Param("a", player.get()),
                            new Param("cluster", dataCluster)
                    )
            ).value("power");

            if (power != null) {
                slash.deferReply().setEmbeds(
                        power.infoLanguage(slash.getUser().getId(), getLanguage(slash))
                ).queue();
            } else
                slash.deferReply(true).setEmbeds(
                        Theme.getMainEmbed()
                                .setDescription(
                                        ThemeEmoji.TIMER.getEmoji().getAsMention() + " " +
                                                Language.message("time-over-random_1", getLanguage(slash)) +
                                                Time.TIMESTAMP_RANDOM_POWER +
                                                Language.message("time-over-random_2", getLanguage(slash)))
                                .build()
                ).queue();
        } else
            replyError(slash);
    }
}
