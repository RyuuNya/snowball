package bot.ryuu.snowball.bot.listener;

import bot.ryuu.snowball.player.Player;
import bot.ryuu.snowball.player.PlayerRepository;
import bot.ryuu.snowball.theme.ThemeMessage;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.HashSet;

@AllArgsConstructor
public class SystemListener extends ListenerAdapter {
    private PlayerRepository playerRepository;

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        super.onGuildJoin(event);

        MessageEmbed commands = ThemeMessage.getMainEmbed()
                .setDescription("""
                                ## Command list
                                
                                `take` - to take one snowball
                                `throw` - throw a snowball at a player
                                `stats` - your statistics
                                `rating` - server rating
                                `random` - gives you a random ability, to use the ability, select it on the action
                                `king` - king of the server on points
                                """).build();

        MessageEmbed rule = ThemeMessage.getMainEmbed()
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
                                """, false).build();

        if (event.getGuild().getDefaultChannel() != null) {
            switch (event.getGuild().getDefaultChannel().getType()) {
                case NEWS -> event.getGuild().getDefaultChannel().asNewsChannel().sendMessage("")
                        .setEmbeds(
                                commands, rule
                        ).queue();
                case TEXT -> event.getGuild().getDefaultChannel().asTextChannel().sendMessage("")
                        .setEmbeds(
                                commands, rule
                        ).queue();
            }
        }
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        super.onGuildMemberJoin(event);

        playerRepository.save(Player.builder()
                .id(event.getMember().getId())
                .idMember(event.getMember().getId())
                .level(0)
                .score(0)
                .server(event.getGuild().getId())
                .objectPowerSet(new HashSet<>())
                .snowballAmount(0)
                .lastRandomObjectPower(LocalDateTime.now().minusMinutes(15))
                .lastTakeSnowball(LocalDateTime.now().minusMinutes(15))
                .build());
    }
}