package bot.ryuu.snowball.bot.commands;

import bot.ryuu.snowball.data.player.Player;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.Optional;

public interface CommandInstruments {
    Optional<Player> getPlayer(SlashCommandInteractionEvent event);

    Optional<Player> getPlayer(String member, String server);

    Optional<Player> getPlayer(Optional<String> member, String server);

    String getLanguage(SlashCommandInteractionEvent event);

    String getLanguage(ButtonInteractionEvent event);

    default Optional<String> getOptionString(SlashCommandInteractionEvent event, String option) {
        if (event.getOption(option) != null)
            return Optional.of(event.getOption(option).getAsString());
        else
            return Optional.empty();
    };

    default Optional<User> getOptionUser(SlashCommandInteractionEvent event, String option) {
        if (event.getOption(option) != null)
            return Optional.of(event.getOption(option).getAsUser());
        else
            return Optional.empty();
    };
}