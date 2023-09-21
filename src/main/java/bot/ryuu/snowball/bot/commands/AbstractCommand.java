package bot.ryuu.snowball.bot.commands;

import bot.ryuu.snowball.data.DataCluster;
import bot.ryuu.snowball.data.player.Player;
import bot.ryuu.snowball.data.server.Server;
import bot.ryuu.snowball.language.Type;
import lombok.Getter;
import lombok.Setter;
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
public class AbstractCommand implements CommandInstruments, CommandReply {
    protected String code;
    protected CommandData commandData;

    protected DataCluster dataCluster;

    public AbstractCommand(DataCluster dataCluster) {
        this.dataCluster = dataCluster;
    }

    public void execute(Object object) {
        if (object instanceof SlashCommandInteractionEvent slash)
            slashInteraction(slash);
        else if (object instanceof ButtonInteractionEvent button)
            buttonInteraction(button);
        else if (object instanceof StringSelectInteractionEvent string)
            stringSelectInteraction(string);
        else if (object instanceof EntitySelectInteractionEvent entity)
            entitySelectInteraction(entity);
        else if (object instanceof ModalInteractionEvent modal)
            modalInteraction(modal);
    }

    protected void slashInteraction(SlashCommandInteractionEvent slash) {
    }

    protected void buttonInteraction(ButtonInteractionEvent button) {
    }

    protected void stringSelectInteraction(StringSelectInteractionEvent string) {
    }

    protected void entitySelectInteraction(EntitySelectInteractionEvent entity) {
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

    @Override
    public Optional<Player> getPlayer(SlashCommandInteractionEvent event) {
        return dataCluster.getPlayerProfile(event);
    }

    @Override
    public Optional<Player> getPlayer(String member, String server) {
        return dataCluster.getPlayerProfile(member, server);
    }

    @Override
    public Optional<Player> getPlayer(Optional<String> member, String server) {
        if (member.isPresent())
            return dataCluster.getPlayerProfile(member.get(), server);
        else
            return Optional.empty();
    }

    @Override
    public String getLanguage(SlashCommandInteractionEvent event) {
        Optional<Server> server = dataCluster.getServer(event.getGuild().getId());

        if (server.isPresent())
            return server.get().getLanguage();
        else
            return "en";
    }

    @Override
    public String getLanguage(ButtonInteractionEvent event) {
        Optional<Server> server = dataCluster.getServer(event.getGuild().getId());

        if (server.isPresent())
            return server.get().getLanguage();
        else
            return "en";
    }
}