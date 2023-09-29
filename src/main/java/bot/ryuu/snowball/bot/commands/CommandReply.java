package bot.ryuu.snowball.bot.commands;

import bot.ryuu.snowball.tools.Theme;
import bot.ryuu.snowball.tools.language.Language;
import bot.ryuu.snowball.tools.language.Messages;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public interface CommandReply {
    default void replyError(SlashCommandInteractionEvent event) {
        event.deferReply(true).setEmbeds(
                Theme.error()
                        .setDescription("error occurred").build()
        ).queue();
    }

    default void replyError(ButtonInteractionEvent event) {
        event.deferReply(true).setEmbeds(
                Theme.error()
                        .setDescription("error occurred").build()
        ).queue();
    }

    default void replyNon(SlashCommandInteractionEvent event) {
        event.deferReply(true).setEmbeds(
                Theme.main()
                        .setDescription("This item of force does not apply to this action").build()
        ).queue();
    }
}