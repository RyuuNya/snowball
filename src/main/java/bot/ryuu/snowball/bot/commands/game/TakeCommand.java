package bot.ryuu.snowball.bot.commands.game;

import bot.ryuu.snowball.bot.commands.AbstractCommand;
import bot.ryuu.snowball.data.DataCluster;
import bot.ryuu.snowball.data.player.Player;
import bot.ryuu.snowball.game.EventAction;
import bot.ryuu.snowball.game.TimeStamp;
import bot.ryuu.snowball.game.event.Event;
import bot.ryuu.snowball.game.event.EventType;
import bot.ryuu.snowball.game.power.Power;
import bot.ryuu.snowball.language.Language;
import bot.ryuu.snowball.theme.Theme;
import bot.ryuu.snowball.theme.ThemeEmoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Optional;

public class TakeCommand extends AbstractCommand {
    public TakeCommand(DataCluster dataCluster) {
        super(dataCluster);

        setCode("_take_command");
        setCommandData(
                Commands.slash("take", "to take one snowball")
                        .addOptions(
                                new OptionData(OptionType.STRING, "power", "power up")
                                        .addChoices(
                                                new Command.Choice("big bags", "BIG_BAGS"),
                                                new Command.Choice("thief", "THIEF")
                                        )
                        )
                        .setGuildOnly(true)
        );
    }

    @Override
    protected void slashInteraction(SlashCommandInteractionEvent slash) {
        super.slashInteraction(slash);

        Optional<Player> a = getPlayer(slash);
        Optional<String> activePower = getOptionString(slash, "power");

        if (a.isPresent()) {
            if (activePower.isPresent())
                switch (activePower.get()) {
                    case "BIG_BAGS" -> a.get().activatePower(Power.BIG_BAGS);
                    case "THIEF" -> a.get().activatePower(Power.THIEF);
                }

            Event<String> event = EventAction.takeSnowball(a.get(), null, dataCluster);

            replySlash(slash, event);
        } else
            replyError(slash);
    }

    private void replySlash(SlashCommandInteractionEvent slash, Event<String> event) {
        String message = Language.message("null", getLanguage(slash));

        switch (event.getType()) {
            case TAKE_SNOWBALL ->
                    message = Language.message("take-snowball", getLanguage(slash));
            case TAKE_SNOWBALL_BIG_BAGS ->
                    message = Language.message("take-snowball-big-bags", getLanguage(slash));
            case TAKE_SNOWBALL_THIEF ->
                    message = Language.message("take-snowball-thief", getLanguage(slash));
            case TIMER_OVER -> message =
                    Language.message("time-over-take_1", getLanguage(slash))
                            + TimeStamp.TIMESTAMP_TAKE_SNOWBALL + Language.message("time-over-take_2", getLanguage(slash));

        }

        slash.deferReply(true).setEmbeds(
                Theme.getMainEmbed()
                        .setDescription(
                                ThemeEmoji.SNOWBALL.getEmoji().getAsMention() + " " +
                                    message
                        ).build()
        ).queue();
    }
}
