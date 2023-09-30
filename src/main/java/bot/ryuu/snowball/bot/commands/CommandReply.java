package bot.ryuu.snowball.bot.commands;

import bot.ryuu.snowball.tools.Theme;
import bot.ryuu.snowball.tools.language.Language;
import bot.ryuu.snowball.tools.language.Messages;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public interface CommandReply {
    default void replyError(SlashCommandInteractionEvent slash, Language lang) {
        slash.deferReply(true).setEmbeds(
                Theme.error()
                        .setDescription(Messages.message("ERROR", lang)).build()
        ).queue();
    }

    default void replyActivate(SlashCommandInteractionEvent slash, Language lang) {
        slash.deferReply(true).setEmbeds(
                Theme.await()
                        .setDescription(Messages.message("ACTIVATE", lang)).build()
        ).queue();
    }
}