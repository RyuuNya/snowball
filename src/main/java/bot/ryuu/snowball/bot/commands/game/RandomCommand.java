package bot.ryuu.snowball.bot.commands.game;

import bot.ryuu.snowball.bot.commands.AbstractCommand;
import bot.ryuu.snowball.data.DataCluster;
import bot.ryuu.snowball.data.player.Player;
import bot.ryuu.snowball.game.EventAction;
import bot.ryuu.snowball.game.TimeStamp;
import bot.ryuu.snowball.game.event.Event;
import bot.ryuu.snowball.game.power.Power;
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
                    .setGuildOnly(true)
        );
    }

    @Override
    protected void slashInteraction(SlashCommandInteractionEvent slash) {
        Optional<Player> player = getPlayer(slash);

        if (player.isPresent()) {
            Event<Power> power = EventAction.randomPower(player.get(), dataCluster);

            if (power.isPresent())
                slash.deferReply().setEmbeds(
                        power.getValue().infoLanguage(slash.getUser().getId(), getLanguage(slash))
                ).queue();
            else
                slash.deferReply(true).setEmbeds(
                        Theme.getMainEmbed()
                                .setDescription(
                                        ThemeEmoji.TIMER.getEmoji().getAsMention() + " " +
                                                Language.message("time-over-random_1", getLanguage(slash)) +
                                                TimeStamp.TIMESTAMP_RANDOM_POWER +
                                                Language.message("time-over-random_2", getLanguage(slash)))
                                .build()
                ).queue();
        } else
            replyError(slash);
    }
}
