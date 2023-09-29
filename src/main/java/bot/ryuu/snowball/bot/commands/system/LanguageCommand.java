package bot.ryuu.snowball.bot.commands.system;

import bot.ryuu.snowball.bot.commands.CommandAbstract;
import bot.ryuu.snowball.data.DataCluster;
import bot.ryuu.snowball.data.server.Server;
import bot.ryuu.snowball.tools.Theme;
import bot.ryuu.snowball.tools.language.Language;
import bot.ryuu.snowball.tools.language.Messages;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.lang.reflect.Member;
import java.util.Optional;

public class LanguageCommand extends CommandAbstract {
    public LanguageCommand(DataCluster cluster) {
        super(cluster);

        setCode("_lang_command");
        setCommand(
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
    public void slashInteraction(SlashCommandInteractionEvent slash) {
        super.slashInteraction(slash);

        Optional<Server> server = cluster.getServer(slash);
        Optional<String> language = getOption(slash, "lang");


        if (server.isPresent() && language.isPresent()) {
            Language l = Language.valueOf(language.get());

            server.get().setLanguage(l)
                    .save(cluster);

            slash.deferReply(true).setEmbeds(
                    Theme.main()
                            .setDescription(Messages.message("SERVER_LANG", lang(slash))).build()
            ).queue();
        } else
            replyError(slash);
    }
}