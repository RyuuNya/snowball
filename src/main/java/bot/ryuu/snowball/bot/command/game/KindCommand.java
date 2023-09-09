package bot.ryuu.snowball.bot.command.game;

import bot.ryuu.snowball.bot.command.AbstractCommand;
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
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.aspectj.weaver.ast.Literal;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Setter
@Getter
public class KindCommand extends AbstractCommand {
    private PlayerRepository playerRepository;

    public KindCommand(PlayerRepository playerRepository) {
        super(playerRepository);

        setCode("_kind_command");
        setCommandData(Commands.slash("kind", "King snowball of this server")
                .setGuildOnly(true));

        setPlayerRepository(playerRepository);
    }

    @Override
    protected void slashCommandInteraction(SlashCommandInteractionEvent event) {
        List<Player> players = playerRepository.findAllByServer(event.getGuild().getId());

        if (players.size() > 0) {
            Collections.sort(players);
            event.deferReply(true).setEmbeds(
                    ThemeMessage.getMainEmbed()
                            .setDescription(ThemeEmoji.KING.getEmoji().getAsMention() +
                                    " The snowball king of this server is <@" + players.get(0).getId() + ">").build()
            ).queue();
        } else
            event.deferReply(true).setEmbeds(
                    ThemeMessage.getErrorEmbed()
                            .setDescription(ThemeEmoji.ERROR.getEmoji().getAsMention() + " error occurred").build()
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
