package bot.ryuu.snowball.bot.commands.system;

import bot.ryuu.snowball.bot.commands.CommandAbstract;
import bot.ryuu.snowball.data.DataCluster;
import bot.ryuu.snowball.tools.Theme;
import bot.ryuu.snowball.tools.language.Messages;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class HelpCommand extends CommandAbstract {

    public HelpCommand(DataCluster cluster) {
        super(cluster);

        setCode("_help_command");
        setCommand(
                Commands.slash("help", "a list of all commands and the game's startup")
                        .setGuildOnly(true)
        );
    }

    @Override
    public void slashInteraction(SlashCommandInteractionEvent slash) {
        super.slashInteraction(slash);

        slash.deferReply(true).setEmbeds(
                Theme.main()
                        .setDescription(
                                Messages.message("HELP_1", lang(slash))
                        ).build(),
                Theme.main()
                        .setDescription(
                                Messages.message("HELP_2", lang(slash))
                        )
                        .addField(
                                Messages.message("HELP_3_TITLE", lang(slash)),
                                Messages.message("HELP_3", lang(slash)),
                                false
                        ).build()
        ).queue();
    }
}