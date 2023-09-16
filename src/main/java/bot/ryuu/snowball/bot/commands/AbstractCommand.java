package bot.ryuu.snowball.bot.commands;

import bot.ryuu.snowball.data.DataCluster;
import bot.ryuu.snowball.data.player.Player;
import bot.ryuu.snowball.data.server.Server;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.Optional;

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

    public void setCommandData(CommandData commandData) {
        this.commandData = commandData;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public CommandData getCommandData() {
        return commandData;
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
    public Optional<String> getOptionString(SlashCommandInteractionEvent event, String option) {
        if (event.getOption(option) != null)
            return Optional.of(event.getOption(option).getAsString());
        else
            return Optional.empty();
    }

    @Override
    public Optional<User> getOptionUser(SlashCommandInteractionEvent event, String option) {
        if (event.getOption(option) != null)
            return Optional.of(event.getOption(option).getAsUser());
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