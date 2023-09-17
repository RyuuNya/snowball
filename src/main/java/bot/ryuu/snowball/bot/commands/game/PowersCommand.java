package bot.ryuu.snowball.bot.commands.game;

import bot.ryuu.snowball.bot.commands.AbstractCommand;
import bot.ryuu.snowball.data.DataCluster;
import bot.ryuu.snowball.game.power.Power;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Optional;

public class PowersCommand extends AbstractCommand {
    public PowersCommand(DataCluster dataCluster) {
        super(dataCluster);

        setCode("_powers_command");
        setCommandData(
                Commands.slash("powers", "all-power information")
                    .addOptions(
                            getPowerOption(true)
                    )
                    .setGuildOnly(true)
        );
    }

    @Override
    protected void slashInteraction(SlashCommandInteractionEvent slash) {
        Optional<String> power = getOptionString(slash, "power");

        Power p = Power.PACIFIER;
        if (power.isPresent())
            switch (power.get()) {
                case "FORTUNE" -> p = Power.FORTUNE;
                case "BIG_BAGS" -> p = Power.BIG_BAGS;
                case "BOOST" -> p = Power.BOOST;
                case "THIEF" -> p = Power.THIEF;
                case "SUPER-THROW" -> p = Power.SUPER_THROW;
                case "ENROLMENT" -> p = Power.ENROLMENT;
            }

        slash.deferReply(true).setEmbeds(
                p.infoCommandLanguage(getLanguage(slash))
        ).queue();
    }
}
