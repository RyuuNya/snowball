package bot.ryuu.snowball.bot.command.game;

import bot.ryuu.snowball.bot.command.AbstractCommand;
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
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.List;
import java.util.Optional;

@Setter
@Getter
public class InventoryCommand extends AbstractCommand {
    private PlayerRepository playerRepository;

    public InventoryCommand(PlayerRepository playerRepository) {
        super(playerRepository);

        setCode("_inventory_command");
        setCommandData(Commands.slash("inventory", "your inventory")
                .setGuildOnly(true));

        setPlayerRepository(playerRepository);
    }

    @Override
    protected void slashCommandInteraction(SlashCommandInteractionEvent event) {
        Optional<Player> player = playerRepository.findById(event.getUser().getId());

        if (player.isPresent()) {
            List<Power> powers = player.get().getObjectPowerSet().stream().toList();
            StringBuilder inventory = new StringBuilder();

            int i = 0;
            while (i < powers.size()) {

            }
            /*
            * # # #|# # #|# # #
            * # A #|# B #|# V #
            * # # #|# # #|# # #
            * -----------------
            *
            * */
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
