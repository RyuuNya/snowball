package bot.ryuu.snowball.bot.commands.game;

import bot.ryuu.snowball.bot.commands.AbstractCommand;
import bot.ryuu.snowball.data.DataCluster;
import bot.ryuu.snowball.data.player.Player;
import bot.ryuu.snowball.gamev2.GameAction;
import bot.ryuu.snowball.gamev2.Time;
import bot.ryuu.snowball.gamev2.event.Param;
import bot.ryuu.snowball.gamev2.event.request.EventRequest;
import bot.ryuu.snowball.gamev2.event.request.Request;
import bot.ryuu.snowball.gamev2.event.response.EventResponse;
import bot.ryuu.snowball.language.LanguageV2;
import bot.ryuu.snowball.language.Type;
import bot.ryuu.snowball.theme.Theme;
import bot.ryuu.snowball.theme.ThemeEmoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.Optional;

public class TakeCommand extends AbstractCommand {
    public TakeCommand(DataCluster dataCluster) {
        super(dataCluster);

        setCode("_take_command");
        setCommandData(
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

        Optional<Player> a = getPlayer(slash);
        Optional<String> activePower = getOptionString(slash, "power");

        if (a.isPresent()) {
            boolean activate = true;
            if (activePower.isPresent())
                activate = GameAction.execute(
                        EventRequest.of(
                                Request.TAKE,
                                new Param("a", a.get()),
                                new Param("power", activePower),
                                new Param("cluster", dataCluster)
                        )
                ).value("activate");


            if (activate) {
                EventResponse event = GameAction.execute(
                        EventRequest.of(
                                Request.TAKE,
                                new Param("a", a.get()),
                                new Param("cluster", dataCluster)
                        )
                );

                replySlash(slash, event);
            } else
                replyNon(slash);

        } else
            replyError(slash);
    }

    private void replySlash(SlashCommandInteractionEvent slash, EventResponse event) {
        String message = LanguageV2.message("NULL", Type.EN);

        switch (event.type()) {
            case TAKE_SNOWBALL ->
                    message = LanguageV2.message("TAKE_SNOWBALL", Type.EN);
            case TAKE_SNOWBALL_BIG_BAGS ->
                    message = LanguageV2.message("TAKE_SNOWBALL_BIG_BAGS", Type.EN);
            case TAKE_SNOWBALL_THIEF ->
                    message = LanguageV2.message("TAKE_SNOWBALL_THIEF", Type.EN);
            case TAKE_SNOWBALL_FORTUNE ->
                    message = LanguageV2.message("TAKE_SNOWBALL_FORTUNE", Type.EN);
            case TIMER_OVER ->
                    message = LanguageV2.message("TAKE_SNOWBALL_TIME_OVER_1", Type.EN)
                            + Time.TIMESTAMP_TAKE_SNOWBALL
                            + LanguageV2.message("TAKE_SNOWBALL_TIME_OVER_2", Type.EN);

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
