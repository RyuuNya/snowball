package bot.ryuu.snowball.bot.commands.system;

import bot.ryuu.snowball.bot.commands.AbstractCommand;
import bot.ryuu.snowball.data.DataCluster;
import bot.ryuu.snowball.data.server.Server;
import bot.ryuu.snowball.language.Language;
import bot.ryuu.snowball.theme.Theme;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Optional;

public class LanguageCommand extends AbstractCommand {
    public LanguageCommand(DataCluster dataCluster) {
        super(dataCluster);

        setCode("_lang_command");
        setCommandData(
                Commands.slash("lang", "server language")
                    .addOptions(
                            new OptionData(OptionType.STRING, "lang", "select the desired language", true)
                                    .addChoice("russia", "RU")
                                    .addChoice("english", "EN")
                    )
                    .setGuildOnly(true)
        );
    }

    @Override
    protected void slashInteraction(SlashCommandInteractionEvent slash) {
        super.slashInteraction(slash);

        Optional<Server> server = dataCluster.getServer(slash.getGuild().getId());
        Optional<String> language = getOptionString(slash, "lang");

        if (server.isPresent() && language.isPresent()) {
            server.get().setLanguage(language.get().toLowerCase())
                    .save(dataCluster.getServerRepository());

            slash.deferReply(true).setEmbeds(
                    Theme.getMainEmbed()
                            .setDescription(Language.message("server-lang", getLanguage(slash))).build()
            ).queue();
        } else
            replyError(slash);
    }
}
