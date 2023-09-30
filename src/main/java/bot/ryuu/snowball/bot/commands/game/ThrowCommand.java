package bot.ryuu.snowball.bot.commands.game;

import bot.ryuu.snowball.bot.commands.CommandAbstract;
import bot.ryuu.snowball.data.DataCluster;
import bot.ryuu.snowball.data.player.Player;
import bot.ryuu.snowball.event.Param;
import bot.ryuu.snowball.event.request.EventRequest;
import bot.ryuu.snowball.event.request.Request;
import bot.ryuu.snowball.event.response.EventResponse;
import bot.ryuu.snowball.game.GameAction;
import bot.ryuu.snowball.game.power.Power;
import bot.ryuu.snowball.tools.Theme;
import bot.ryuu.snowball.tools.language.Language;
import bot.ryuu.snowball.tools.language.Messages;
import bot.ryuu.snowball.tools.language.ThemeEmoji;
import bot.ryuu.snowball.tools.register.Registration;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.List;
import java.util.Optional;

public class ThrowCommand extends CommandAbstract {
    public ThrowCommand(DataCluster cluster) {
        super(cluster);

        setCode("_throw_command");
        setCommand(
                Commands.slash("throw", "throw a snowball at a player")
                        .addOption(OptionType.USER, "user", "user you want to throw a snowball at", true)
                        .addOptions(
                                getOptionPower(Request.THROW, false)
                        )
                        .setGuildOnly(true)
        );
    }

    @Override
    protected void slashInteraction(SlashCommandInteractionEvent slash) {
        super.slashInteraction(slash);

        Optional<User> u = getOption(slash, "user");

        Optional<Player> a = cluster.getPlayer(slash);
        Optional<Player> b = cluster.getPlayer(u.get().getId(), slash.getGuild().getId());

        Optional<String> activePower = getOption(slash, "power");

        if (a.isEmpty())
            a = Registration.register(slash.getUser(), slash.getGuild(), cluster);

        if (b.isEmpty())
            b = Registration.register(u.get(), slash.getGuild(), cluster);

        if (a.isPresent() && b.isPresent()) {
            boolean activate = GameAction.execute(
                    EventRequest.of(
                            Request.ACTIVATE,
                            new Param("a", a.get()),
                            new Param("power", Power.valueOf(activePower.orElse("NULL"))),
                            new Param("cluster", cluster)
                    )
            ).valueNoOptional("activate");


            if (activate) {
                EventResponse event = GameAction.execute(
                        EventRequest.of(
                                Request.THROW,
                                new Param("a", a.get()),
                                new Param("b", b.get()),
                                new Param("cluster", cluster)
                        )
                );

                replySlash(slash, event, a.get(), b.get());
            } else
                replyActivate(slash, lang(slash));
        } else
            replyError(slash, lang(slash));
    }

    private void replySlash(SlashCommandInteractionEvent slash, EventResponse event, Player a, Player b) {
        String message = Messages.message("NULL", Language.EN);

        switch (event.type()) {
            case HIT ->
                    message = "<@" + a.getMember() + Messages.message("HIT", lang(slash))
                            + b.getMember() + ">";
            case HIT_SUPER_THROW -> {
                message = "<@" + a.getMember() + Messages.message("HIT", lang(slash));

                List<Player> players = event.valueNoOptional("players");

                for (Player player : players) {
                    message += " <@" + player.getMember() + "> ";
                }
            }
            case HIT_ENROLMENT -> {
                Player c = event.valueNoOptional("c");

                assert c != null;
                message = "<@" + b.getMember() + Messages.message("HIT", lang(slash))
                        + c.getMember() + ">";
            }
            case MISSED ->
                    message = "<@" + a.getMember() + Messages.message("MISSED_1", lang(slash))
                            + b.getMember() + Messages.message("MISSED_2", lang(slash));
            case MISSED_ENROLMENT -> {
                Player c = event.valueNoOptional("c");

                assert c != null;
                message = "<@" + b.getMember() + Messages.message("MISSED_1", lang(slash))
                        + c.getMember() + Messages.message("MISSED_2", lang(slash));
            }
            case THROW_SNOWBALL_LIMIT ->
                    message = Messages.message("SNOWBALL_EMPTY", lang(slash));
        }

        slash.deferReply().setEmbeds(
                Theme.main()
                        .setDescription(
                                ThemeEmoji.THROW.getEmoji().getAsMention() + " " +
                                        message
                        ).build()
        ).queue();
    }
}