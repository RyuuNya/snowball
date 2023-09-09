package bot.ryuu.snowball.bot.command;

import bot.ryuu.snowball.player.PlayerRepository;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

@Getter
@Setter
public abstract class AbstractCommand {
    private String code;

    private CommandData commandData;

    public AbstractCommand(PlayerRepository playerRepository) {

    }

    public void execute(Object object) {
        if (object instanceof SlashCommandInteractionEvent slash)
            slashCommandInteraction(slash);
        else if (object instanceof ButtonInteractionEvent button)
            buttonCommandInteraction(button);
        else if (object instanceof StringSelectInteractionEvent string)
            stringSelectCommandInteraction(string);
        else if (object instanceof EntitySelectInteractionEvent entity)
            entitySelectCommandInteraction(entity);
        else if (object instanceof ModalInteractionEvent modal)
            modalCommandInteraction(modal);
    }

    protected abstract void slashCommandInteraction(SlashCommandInteractionEvent event);

    protected abstract void buttonCommandInteraction(ButtonInteractionEvent event);

    protected abstract void stringSelectCommandInteraction(StringSelectInteractionEvent event);

    protected abstract void entitySelectCommandInteraction(EntitySelectInteractionEvent event);

    protected abstract void modalCommandInteraction(ModalInteractionEvent event);

    /** Service methods */
    protected String getInteractionId(String id) {
        return id.substring(0, id.indexOf('*'));
    }

    protected String getContentId(String id) {
        return id.substring(id.indexOf('*') + 1);
    }
}
