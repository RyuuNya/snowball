package bot.ryuu.snowball.bot.command.game;

import bot.ryuu.snowball.bot.command.AbstractCommand;
import bot.ryuu.snowball.game.Event;
import bot.ryuu.snowball.game.EventAction;
import bot.ryuu.snowball.game.PowerSystem;
import bot.ryuu.snowball.game.TimeStamp;
import bot.ryuu.snowball.game.power.Power;
import bot.ryuu.snowball.player.Player;
import bot.ryuu.snowball.player.PlayerRepository;
import bot.ryuu.snowball.theme.ThemeEmoji;
import bot.ryuu.snowball.theme.ThemeMessage;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Optional;

@Getter
@Setter
public class TakeCommand extends AbstractCommand {
    private PlayerRepository playerRepository;

    public TakeCommand(PlayerRepository playerRepository) {
        super(playerRepository);

        setCode("_take");
        setCommandData(Commands.slash("take", "to take one snowball")
                .addOptions(
                        new OptionData(OptionType.STRING, "power", "power up")
                                .addChoices(
                                        new Command.Choice("big bags", "BIG_BAGS"),
                                        new Command.Choice("thief", "THIEF")
                                )
                )
                .addOption(OptionType.USER, "user", "the user from whom you want to take the snowballs")
                .setGuildOnly(true));

        setPlayerRepository(playerRepository);
    }

    @Override
    protected void slashCommandInteraction(SlashCommandInteractionEvent event) {
        Optional<Player> a = playerRepository.findById(event.getUser().getId());

        Optional<Player> b = Optional.empty();
        if (event.getOption("user") != null) {
            b = playerRepository.findById(event.getOption("user").getAsUser().getId());
        }

        String activePower = null;
        if (event.getOption("power") != null) {
            activePower = event.getOption("power").getAsString();
        }

        if (a.isPresent()) {
            if (activePower != null) {
                switch (activePower) {
                    case "BIG_BAGS" -> PowerSystem.putActiveObject(a.get(), Power.BIG_BAGS);
                    case "THIEF" -> PowerSystem.putActiveObject(a.get(), Power.THIEF);
                }
            }

            Event e = EventAction.takeSnowball(a.get(), b.orElse(null));

            replyMessage(e, event);
        } else
            event.deferReply(true).setEmbeds(
                    ThemeMessage.getErrorEmbed()
                            .setDescription(ThemeEmoji.ERROR.getEmoji().getAsMention() + " error occurred").build()
            ).queue();
    }

    private void replyMessage(Event e, SlashCommandInteractionEvent event) {
        String message = "Hmm, nothing happened";

        switch (e) {
            case TAKE_SNOWBALL -> message = "You picked up one snowball";
            case TAKE_SNOWBALL_BIG_BAGS -> message = "You picked up 3 snowballs";
            case TAKE_SNOWBALL_THIEF -> message = "you stole half the snowballs from a player";
            case TIMER_OVER -> message = "you've already taken a snowball, wait " +
                    TimeStamp.TIMESTAMP_TAKE_SNOWBALL + " minutes to take another one";
        }

        event.deferReply(true).setEmbeds(
                ThemeMessage.getMainEmbed()
                        .setDescription(ThemeEmoji.SNOWBALL.getEmoji().getAsMention() + " " + message).build()
        ).queue();
    }

    @Override
    protected void buttonCommandInteraction(ButtonInteractionEvent event) {

    }

    @Override
    protected void stringSelectCommandInteraction(StringSelectInteractionEvent event) {

    }

    @Override
    protected void entitySelectCommandInteraction(EntitySelectInteractionEvent event) {

    }

    @Override
    protected void modalCommandInteraction(ModalInteractionEvent event) {

    }
}
