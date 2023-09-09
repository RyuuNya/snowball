package bot.ryuu.snowball.bot.command.game;

import bot.ryuu.snowball.bot.command.AbstractCommand;
import bot.ryuu.snowball.game.power.Power;
import bot.ryuu.snowball.player.PlayerRepository;
import bot.ryuu.snowball.theme.ThemeMessage;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class PowersCommand extends AbstractCommand {
    public PowersCommand(PlayerRepository playerRepository) {
        super(playerRepository);

        setCode("_powers_command");
        setCommandData(Commands.slash("powers", "all-power information")
                .addOptions(
                        new OptionData(OptionType.STRING, "power", "subject that interests you", true)
                                .addChoice("fortune", "FORTUNE")
                                .addChoice("big bags", "BIG_BAGS")
                                .addChoice("boost", "BOOST")
                                .addChoice("thief", "THIEF")
                )
                .setGuildOnly(true));
    }

    @Override
    protected void slashCommandInteraction(SlashCommandInteractionEvent event) {
        String power = event.getOption("power").getAsString();

        Power p = Power.PACIFIER;
        switch (power) {
            case "FORTUNE" -> p = Power.FORTUNE;
            case "BIG_BAGS" -> p = Power.BIG_BAGS;
            case "BOOST" -> p = Power.BOOST;
            case "THIEF" -> p = Power.THIEF;
        }
        
        event.deferReply(true).setEmbeds(
                p.infoCommand()
        ).queue();
    }

    @Override
    protected void buttonCommandInteraction(ButtonInteractionEvent event) {

    }

    @Override
    protected void stringSelectCommandInteraction(StringSelectInteractionEvent event) {

    }

    @Override
    protected void entitySelectCommandInteraction(EntitySelectInteractionEvent event) {

    }

    @Override
    protected void modalCommandInteraction(ModalInteractionEvent event) {

    }
}
