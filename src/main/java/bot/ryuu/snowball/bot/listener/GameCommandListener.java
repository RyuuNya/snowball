package bot.ryuu.snowball.bot.listener;

import bot.ryuu.snowball.bot.commands.AbstractCommand;
import lombok.Setter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

@Setter
public class GameCommandListener extends ListenerAdapter {
    private List<AbstractCommand> commands;

    public GameCommandListener(List<AbstractCommand> commands) {
        setCommands(commands);
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        super.onButtonInteraction(event);

        for (AbstractCommand command : commands) {
            if (Objects.requireNonNull(event.getButton().getId()).contains(command.getCommandData().getName())) {
                command.execute(event);
            }
        }
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        super.onSlashCommandInteraction(event);

        for (AbstractCommand command : commands) {
            if (event.getName().equals(command.getCommandData().getName())) {
                command.execute(event);
            }
        }

    }
}
