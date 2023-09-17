package bot.ryuu.snowball.bot.commands.game;

import bot.ryuu.snowball.bot.commands.AbstractCommand;
import bot.ryuu.snowball.data.DataCluster;
import bot.ryuu.snowball.data.player.Player;
import bot.ryuu.snowball.game.power.Power;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Optional;

public class GiveCommand extends AbstractCommand {
    public GiveCommand(DataCluster dataCluster) {
        super(dataCluster);

        setCode("_give_command");
        setCommandData(
                Commands.slash("give", "give power")
                        .addOptions(
                                new OptionData(OptionType.STRING, "power", "subject that interests you", true)
                                        .addChoice("fortune", "FORTUNE")
                                        .addChoice("big bags", "BIG_BAGS")
                                        .addChoice("boost", "BOOST")
                                        .addChoice("thief", "THIEF")
                                        .addChoice("super throw", "SUPER-THROW")
                                        .addChoice("pacifier", "PACIFIER")
                                        .addChoice("enrolment", "ENROLMENT")
                        )
                        .setGuildOnly(true)
        );
    }

    @Override
    protected void slashInteraction(SlashCommandInteractionEvent slash) {
        super.slashInteraction(slash);

        Optional<Player> player = getPlayer(slash);
        Optional<String> power = getOptionString(slash, "power");

        if (player.isPresent() && power.isPresent()) {
            switch (power.get()) {
                case "FORTUNE" -> player.get().addPower(Power.FORTUNE);
                case "BIG_BAGS" -> player.get().addPower(Power.BIG_BAGS);
                case "BOOST" -> player.get().addPower(Power.BOOST);
                case "THIEF" -> player.get().addPower(Power.THIEF);
                case "SUPER-THROW" -> player.get().addPower(Power.SUPER_THROW);
                case "ENROLMENT" -> player.get().addPower(Power.ENROLMENT);
            }

            player.get().save(dataCluster.getPlayerRepository());

            slash.reply("power was given").setEphemeral(true).queue();
        } else
            replyError(slash);
    }
}
