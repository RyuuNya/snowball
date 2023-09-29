package bot.ryuu.snowball.bot.commands;

import bot.ryuu.snowball.data.DataCluster;
import bot.ryuu.snowball.data.server.Server;
import bot.ryuu.snowball.tools.language.Language;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Optional;

@Getter
@Setter
public abstract class CommandAbstract implements CommandInstruments, CommandReply {
    protected String code;
    protected CommandData command;

    protected DataCluster cluster;

    public CommandAbstract(DataCluster cluster) {
        this.cluster = cluster;
    }

    public void execute(GenericEvent event) {
        switch (event) {
            case SlashCommandInteractionEvent slash ->
                slashInteraction(slash);
            case ButtonInteractionEvent button ->
                buttonInteraction(button);
            case StringSelectInteractionEvent select ->
                stringSelectInteraction(select);
            case EntitySelectInteractionEvent select ->
                entitySelectInteraction(select);
            case ModalInteractionEvent modal ->
                modalInteraction(modal);
            case null, default -> throw new IllegalStateException("Unexpected value: " + event);
        }
    }

    protected void slashInteraction(SlashCommandInteractionEvent slash) {

    }

    protected void buttonInteraction(ButtonInteractionEvent button) {

    }

    protected void stringSelectInteraction(StringSelectInteractionEvent select) {

    }

    protected void entitySelectInteraction(EntitySelectInteractionEvent select) {

    }

    protected void modalInteraction(ModalInteractionEvent modal) {

    }

    protected OptionData getPowerOption(Boolean required) {
        return new OptionData(OptionType.STRING, "power", "subject that interests you", required)
                .addChoice("fortune", "FORTUNE")
                .addChoice("big bags", "BIG_BAGS")
                .addChoice("boost", "BOOST")
                .addChoice("thief", "THIEF")
                .addChoice("super throw", "SUPER_THROW")
                .addChoice("pacifier", "PACIFIER")
                .addChoice("enrolment", "ENROLMENT");
    }

    public Language lang(SlashCommandInteractionEvent slash) {
        Optional<Server> server = cluster.getServer(slash);

        if (server.isPresent())
            return server.get().getLanguage();
        else
            return Language.EN;
    }

    public Language lang(ButtonInteractionEvent button) {
        Optional<Server> server = cluster.getServer(button);

        if (server.isPresent())
            return server.get().getLanguage();
        else
            return Language.EN;
    }
}