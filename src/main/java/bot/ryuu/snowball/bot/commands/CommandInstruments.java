package bot.ryuu.snowball.bot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.Optional;

public interface CommandInstruments {
    default <T> Optional<T> getOption(SlashCommandInteractionEvent slash, String name) {
        if (slash.getOption(name) == null)
            return Optional.empty();

        switch (slash.getOption(name).getType()) {
            case STRING -> {
                return Optional.of((T) slash.getOption(name).getAsString());
            }
            case USER -> {
                return Optional.of((T) slash.getOption(name).getAsUser());
            }
            case ROLE -> {
                return Optional.of((T) slash.getOption(name).getAsRole());
            }
            default -> {
                return Optional.empty();
            }
        }
    }
}