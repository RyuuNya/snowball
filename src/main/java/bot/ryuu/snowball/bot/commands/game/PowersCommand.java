package bot.ryuu.snowball.bot.commands.game;

import bot.ryuu.snowball.bot.commands.CommandAbstract;
import bot.ryuu.snowball.data.DataCluster;
import bot.ryuu.snowball.event.request.Request;
import bot.ryuu.snowball.game.power.Power;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.Optional;

public class PowersCommand extends CommandAbstract {
    public PowersCommand(DataCluster cluster) {
        super(cluster);

        setCode("_powers_command");
        setCommand(
                Commands.slash("powers", "all-power information")
                        .addOptions(
                                getOptionPower(Request.POWER, true)
                        )
                        .setGuildOnly(true)
        );
    }

    @Override
    protected void slashInteraction(SlashCommandInteractionEvent slash) {
        super.slashInteraction(slash);

        Optional<String> power = getOption(slash, "power");

        if (power.isPresent()) {
            Power p = Power.valueOf(power.get().toUpperCase());

            slash.deferReply(true).setEmbeds(
                    p.info(lang(slash))
            ).queue();
        } else
            replyError(slash, lang(slash));
    }
}