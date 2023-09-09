package bot.ryuu.snowball.bot.command.game;

import bot.ryuu.snowball.bot.command.AbstractCommand;
import bot.ryuu.snowball.game.Event;
import bot.ryuu.snowball.game.EventAction;
import bot.ryuu.snowball.game.PowerSystem;
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
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Optional;

@Getter
@Setter
public class ThrowCommand extends AbstractCommand {
    private PlayerRepository playerRepository;

    public ThrowCommand(PlayerRepository playerRepository) {
        super(playerRepository);

        setCode("_throw");
        setCommandData(Commands.slash("throw", "snowball the user")
                .addOption(OptionType.USER, "user", "user you want to throw a snowball at", true)
                .addOptions(
                        new OptionData(OptionType.STRING, "power", "power up")
                                .addChoice("fortune", "FORTUNE")
                )
                .setGuildOnly(true));

        setPlayerRepository(playerRepository);
    }

    @Override
    protected void slashCommandInteraction(SlashCommandInteractionEvent event) {
        Optional<Player> a = playerRepository.findById(event.getUser().getId());
        Optional<Player> b = playerRepository.findById(event.getOption("user").getAsUser().getId());

        String activePower = null;
        if (event.getOption("power") != null)
            activePower = event.getOption("power").getAsString();

        if (a.isPresent() && b.isPresent()) {
            if (activePower != null) {
                switch (activePower) {
                    case "FORTUNE" -> PowerSystem.putActiveObject(a.get(), Power.FORTUNE);
                }
            }

            Event e = EventAction.throwSnowball(a.get(), b.get());

            replyMessage(a.get(), b.get(), e, event);
        } else
            event.deferReply(true).setEmbeds(
                    ThemeMessage.getErrorEmbed()
                            .setDescription(ThemeEmoji.ERROR.getEmoji().getAsMention() + " error occurred").build()
            ).queue();
    }

    private void replyMessage(Player a, Player b, Event e, SlashCommandInteractionEvent event) {
        String content = "Hmm, nothing happened";

        switch (e) {
            case HIT -> content = "<@" + a.getId() + "> hits a snowball into <@" + b.getId() + ">";
            case MISSED -> content = "<@" + a.getId() + "> wanted to throw a snowball at <@" + b.getId() + ">, but missed";
        }

        event.deferReply(true).setContent(
                (e == Event.HIT) ? "<@" + b.getId() + ">" : ""
        ).setEmbeds(
                ThemeMessage.getMainEmbed()
                        .setDescription(ThemeEmoji.THROW.getEmoji().getAsMention() + " " + content).build()
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
