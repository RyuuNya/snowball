package bot.ryuu.snowball.bot.command.system;

import bot.ryuu.snowball.bot.command.AbstractCommand;
import bot.ryuu.snowball.player.Player;
import bot.ryuu.snowball.player.PlayerRepository;
import bot.ryuu.snowball.theme.ThemeMessage;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.List;
import java.util.Optional;

@Setter
@Getter
public class ResetCommand extends AbstractCommand {
    private PlayerRepository playerRepository;

    public ResetCommand(PlayerRepository playerRepository) {
        super(playerRepository);

        setCode("_reset_command");
        setCommandData(Commands.slash("reset", "resets all users")
                .setGuildOnly(true));

        setPlayerRepository(playerRepository);
    }

    @Override
    protected void slashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getGuild().getOwner().equals(event.getUser())) {
            List<Player> players = playerRepository.findAllByServer(event.getGuild().getId());

            players.forEach(player -> {
                player.setScore(0);
                player.setLevel(0);
                player.setSnowballAmount(0);
            });

            playerRepository.saveAll(players);

            event.deferReply(true).setEmbeds(
                    ThemeMessage.getMainEmbed()
                            .setDescription("User data has been reset").build()
            ).queue();
        } else
            event.deferReply(true).setEmbeds(
                    ThemeMessage.getMainEmbed()
                            .setDescription("You have insufficient rights or an error has occurred").build()
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
