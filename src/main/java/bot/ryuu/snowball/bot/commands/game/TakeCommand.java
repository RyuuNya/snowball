package bot.ryuu.snowball.bot.commands.game;

import bot.ryuu.snowball.bot.commands.CommandAbstract;
import bot.ryuu.snowball.data.DataCluster;
import bot.ryuu.snowball.data.player.Player;
import bot.ryuu.snowball.event.Param;
import bot.ryuu.snowball.event.request.EventRequest;
import bot.ryuu.snowball.event.request.Request;
import bot.ryuu.snowball.event.response.EventResponse;
import bot.ryuu.snowball.game.GameAction;
import bot.ryuu.snowball.game.Time;
import bot.ryuu.snowball.tools.Theme;
import bot.ryuu.snowball.tools.language.Language;
import bot.ryuu.snowball.tools.language.Messages;
import bot.ryuu.snowball.tools.language.ThemeEmoji;
import bot.ryuu.snowball.tools.register.Registration;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.lang.reflect.Member;
import java.util.Optional;

public class TakeCommand extends CommandAbstract {
    public TakeCommand(DataCluster cluster) {
        super(cluster);

        setCode("_take_command");
        setCommand(
                Commands.slash("take", "to take one snowball")
                        .addOptions(
                                getPowerOption(false)
                        )
                        .setGuildOnly(true)
        );
    }

    @Override
    protected void slashInteraction(SlashCommandInteractionEvent slash) {
        super.slashInteraction(slash);

        Optional<Player> a = cluster.getPlayer(slash);
        Optional<String> activePower = getOption(slash, "power");

        if (a.isEmpty()) {
            a = Registration.register(slash.getUser(), slash.getGuild(), cluster);
        }

        if (a.isPresent()) {
            boolean activate = true;
            if (activePower.isPresent())
                activate = GameAction.execute(
                        EventRequest.of(
                                Request.ACTIVATE,
                                new Param("action", Request.TAKE),
                                new Param("a", a.get()),
                                new Param("power", activePower.get()),
                                new Param("cluster", cluster)
                        )
                ).valueNoOptional("activate");

            if (activate) {
                EventResponse event = GameAction.execute(
                        EventRequest.of(
                                Request.TAKE,
                                new Param("a", a.get()),
                                new Param("cluster", cluster)
                        )
                );

                replySlash(slash, event);
            } else
                replyNon(slash);
        } else
            replyError(slash);
    }

    private void replySlash(SlashCommandInteractionEvent slash, EventResponse event) {
        String message = Messages.message("NULL", Language.EN);

        switch (event.type()) {
            case TAKE_SNOWBALL ->
                    message = Messages.message("TAKE_SNOWBALL", lang(slash));
            case TAKE_SNOWBALL_BOOST ->
                    message = Messages.message("TAKE_SNOWBALL_BOOST", lang(slash));
            case TAKE_SNOWBALL_BIG_BAGS ->
                    message = Messages.message("TAKE_SNOWBALL_BIG_BAGS", lang(slash));
            case TAKE_SNOWBALL_THIEF ->
                    message = Messages.message("TAKE_SNOWBALL_THIEF", lang(slash));
            case TAKE_SNOWBALL_FORTUNE ->
                    message = Messages.message("TAKE_SNOWBALL_FORTUNE", lang(slash));
            case TIMER_OVER ->
                    message = Messages.message("TAKE_SNOWBALL_TIME_OVER_1", lang(slash))
                            + Time.TIMESTAMP_TAKE_SNOWBALL
                            + Messages.message("TAKE_SNOWBALL_TIME_OVER_2", lang(slash));

        }

        slash.deferReply(true).setEmbeds(
                Theme.main()
                        .setDescription(
                                ThemeEmoji.SNOWBALL.getEmoji().getAsMention() + " " +
                                        message
                        ).build()
        ).queue();
    }
}