package bot.ryuu.snowball.bot.commands.game;

import bot.ryuu.snowball.bot.commands.AbstractCommand;
import bot.ryuu.snowball.data.DataCluster;
import bot.ryuu.snowball.data.player.Player;
import bot.ryuu.snowball.game.EventAction;
import bot.ryuu.snowball.game.event.Event;
import bot.ryuu.snowball.game.event.request.EventRequest;
import bot.ryuu.snowball.game.event.request.Request;
import bot.ryuu.snowball.game.event.request.RequestBody;
import bot.ryuu.snowball.game.event.response.EventResponse;
import bot.ryuu.snowball.game.event.response.Response;
import bot.ryuu.snowball.game.power.Power;
import bot.ryuu.snowball.language.Language;
import bot.ryuu.snowball.theme.Theme;
import bot.ryuu.snowball.theme.ThemeEmoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ThrowCommand extends AbstractCommand {
    public ThrowCommand(DataCluster dataCluster) {
        super(dataCluster);

        setCode("_throw_command");
        setCommandData(
                Commands.slash("throw", "throw a snowball at a player")
                    .addOption(OptionType.USER, "user", "user you want to throw a snowball at", true)
                    .addOptions(
                            getPowerOption(false)
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
            boolean activate = true;

            if (activePower.isPresent())
                activate = EventAction.activatePower(
                        EventRequest.of(Request.TAKE, new RequestBody(a.get(), null, dataCluster), activePower.get())
                );


            if (activate) {
                EventResponse event = EventAction.throwSnowball(a.get(), b.get(), dataCluster);

                replySlash(slash, event, a.get(), b.get());
            } else
                replyNon(slash);
        } else
            replyError(slash);
    }

    private void replySlash(SlashCommandInteractionEvent slash, EventResponse event, Player a, Player b) {
        String message = Language.message("null", getLanguage(slash));

        switch (event.getType()) {
            case HIT ->
                    message = "<@" + a.getMember() + Language.message("hit", getLanguage(slash))
                            + b.getMember() + ">";
            case HIT_SUPER_THROW -> {
                    message = "<@" + a.getMember() + Language.message("hit", getLanguage(slash));

                    List<Player> players = event.staticCast();

                    for (Player player : Objects.requireNonNull(players)) {
                        message += " <@" + player.getMember() + "> ";
                    }
            }
            case HIT_ENROLMENT -> {
                Player c = event.staticCast();

                assert c != null;
                message = "<@" + b.getMember() + Language.message("hit", getLanguage(slash))
                        + c.getMember() + ">";
            }
            case MISSED ->
                    message = "<@" + a.getMember() + Language.message("missed_1", getLanguage(slash))
                            + b.getMember() +
                            Language.message("missed_2", getLanguage(slash));
            case MISSED_ENROLMENT -> {
                    Player c = event.staticCast();

                    assert c != null;

                    message = "<@" + b.getMember() + Language.message("missed_1", getLanguage(slash))
                            + c.getMember() +
                            Language.message("missed_2", getLanguage(slash));
            }

            case SNOWBALL_LIMIT ->
                    message = Language.message("snowball-empty", getLanguage(slash));
        }

        slash.deferReply(!(event.getType() == Response.MISSED || event.getType() == Response.HIT)).setContent(
                (event.getType() == Response.HIT || event.getType() == Response.MISSED) ?
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
