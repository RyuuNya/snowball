package bot.ryuu.snowball.bot.command.system;

import bot.ryuu.snowball.bot.command.AbstractCommand;
import bot.ryuu.snowball.player.PlayerRepository;
import bot.ryuu.snowball.theme.ThemeMessage;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class HelpCommand extends AbstractCommand {

    public HelpCommand(PlayerRepository playerRepository) {
        super(playerRepository);

        setCode("_help_command");
        setCommandData(Commands.slash("help", "a list of all commands and the game's startup")
                .setGuildOnly(true));
    }

    @Override
    protected void slashCommandInteraction(SlashCommandInteractionEvent event) {
        event.deferReply(true).setEmbeds(
                ThemeMessage.getMainEmbed()
                        .setDescription("""
                                ## Command list
                                
                                `take` - to take one snowball
                                `throw` - throw a snowball at a player
                                `stats` - your statistics
                                `rating` - server rating
                                `random` - gives you a random ability, to use the ability, select it on the action
                                `king` - king of the server on points
                                """).build(),
                ThemeMessage.getMainEmbed()
                        .setDescription("""
                                ## Welcome to the game - snowball
                                                                
                                The rules of the game are simple, you take a snowball and throw it at another player.
                                                                
                                To pick up a snowball use the take command, to throw a snowball use the throw command.
                                """)
                        .addField("Powers", """
                                To make the game was not boring use the New Year's power.
                                
                                `fortune` - increases the chance of hitting a player to 100%
                                `big bags` - you pick up 3 snowballs at a time
                                `thief` - you steal half of another player's snowballs (mandatory - when using power, specify the player)
                                `boost` - you get more points for hitting a player
                                """, false).build()
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
