package bot.ryuu.snowball.bot.commands.game;

import bot.ryuu.snowball.bot.commands.AbstractCommand;
import bot.ryuu.snowball.data.DataCluster;
import bot.ryuu.snowball.data.player.Player;
import bot.ryuu.snowball.game.EventAction;
import bot.ryuu.snowball.game.event.Event;
import bot.ryuu.snowball.game.event.EventType;
import bot.ryuu.snowball.game.power.Power;
import bot.ryuu.snowball.language.Language;
import bot.ryuu.snowball.theme.Theme;
import bot.ryuu.snowball.theme.ThemeEmoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Optional;

public class ThrowCommand extends AbstractCommand {
    public ThrowCommand(DataCluster dataCluster) {
        super(dataCluster);

        setCode("_throw_command");
        setCommandData(
                Commands.slash("throw", "throw a snowball at a player")
                    .addOption(OptionType.USER, "user", "user you want to throw a snowball at", true)
                    .addOptions(
                            new OptionData(OptionType.STRING, "power", "power up")
                                    .addChoice("fortune", "FORTUNE")
                    )
                    .setGuildOnly(true)
        );
    }

    @Override
    protected void slashInteraction(SlashCommandInteractionEvent slash) {
        super.slashInteraction(slash);

        Optional<Player> a = getPlayer(slash);
        Optional<Player> b = getPlayer(getOptionString(slash, "user"), slash.getGuild().getId());

        Optional<String> activePower = getOptionString(slash, "power");

        if (a.isPresent() && b.isPresent()) {
            if (activePower.isPresent())
                switch (activePower.get()) {
                    case "FORTUNE" -> a.get().activatePower(Power.FORTUNE);
                }

            Event<String> event = EventAction.throwSnowball(a.get(), b.get(), dataCluster);

            replySlash(slash, event, a.get(), b.get());
        } else
            replyError(slash);
    }

    private void replySlash(SlashCommandInteractionEvent slash, Event<String> event, Player a, Player b) {
        String message = Language.message("null", getLanguage(slash));

        switch (event.getType()) {
            case HIT ->
                    message = "<@" + a.getMember() + Language.message("hit", getLanguage(slash))
                            + b.getMember() + ">";
            case MISSED ->
                    message = "<@" + a.getMember() + Language.message("missed_1", getLanguage(slash))
                            + b.getMember() +
                            Language.message("missed_2", getLanguage(slash));
            case SNOWBALL_LIMIT ->
                    message = Language.message("snowball-empty", getLanguage(slash));
        }

        slash.deferReply(!(event.getType() == EventType.MISSED || event.getType() == EventType.HIT)).setContent(
                (event.getType() == EventType.HIT || event.getType() == EventType.MISSED) ?
                        "<@" + b.getMember() + ">" : ""
        ).setEmbeds(
                Theme.getMainEmbed()
                        .setDescription(
                                ThemeEmoji.THROW.getEmoji().getAsMention() + " " +
                                        message
                        ).build()
        ).queue();
    }
}
