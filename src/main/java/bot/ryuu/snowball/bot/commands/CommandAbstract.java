package bot.ryuu.snowball.bot.commands;

import bot.ryuu.snowball.data.DataCluster;
import bot.ryuu.snowball.data.server.Server;
import bot.ryuu.snowball.event.request.Request;
import bot.ryuu.snowball.tools.language.Language;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.awt.*;
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

    protected OptionData getOptionPower(Request request, boolean required) {
        OptionData option = new OptionData(OptionType.STRING, "power", "subject that interests you", required);

        switch (request) {
            case TAKE -> option.addChoice("Fortune", "FORTUNE")
                    .addChoice("Big bags", "BIG_BAGS")
                    .addChoice("Thief", "THIEF")
                    .addChoice("Boost", "BOOST");

            case THROW -> option.addChoice("Fortune", "FORTUNE")
                    .addChoice("Boost", "BOOST")
                    .addChoice("Enrolment", "ENROLMENT")
                    .addChoice("Super throw", "SUPER_THROW");

            case POWER -> option.addChoice("Fortune", "FORTUNE")
                    .addChoice("Big bags", "BIG_BAGS")
                    .addChoice("Thief", "THIEF")
                    .addChoice("Boost", "BOOST")
                    .addChoice("Enrolment", "ENROLMENT")
                    .addChoice("Pacifier", "PACIFIER")
                    .addChoice("Super throw", "SUPER_THROW");
        }

        return option;
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