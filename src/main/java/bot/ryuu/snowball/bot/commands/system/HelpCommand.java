package bot.ryuu.snowball.bot.commands.system;

import bot.ryuu.snowball.bot.commands.AbstractCommand;
import bot.ryuu.snowball.data.DataCluster;
import bot.ryuu.snowball.language.Language;
import bot.ryuu.snowball.theme.Theme;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class HelpCommand extends AbstractCommand {
    public HelpCommand(DataCluster dataCluster) {
        super(dataCluster);

        setCode("_help_command");
        setCommandData(
                Commands.slash("help", "a list of all commands and the game's startup")
                    .setGuildOnly(true)
        );
    }

    @Override
    protected void slashInteraction(SlashCommandInteractionEvent slash) {
        super.slashInteraction(slash);

        slash.deferReply(true).setEmbeds(
                Theme.getMainEmbed()
                        .setDescription(
                                Language.message("help_1", getLanguage(slash))
                        ).build(),
                Theme.getMainEmbed()
                        .setDescription(
                                Language.message("help_2", getLanguage(slash))
                        )
                        .addField(
                                Language.message("help_3-title", getLanguage(slash)),
                                Language.message("help_3", getLanguage(slash)),
                                false
                        ).build()
        ).queue();
    }
}
