package bot.ryuu.snowball.game.power;

import bot.ryuu.snowball.game.Event;
import bot.ryuu.snowball.game.PowerSystem;
import bot.ryuu.snowball.player.Player;
import bot.ryuu.snowball.player.PlayerRepository;
import bot.ryuu.snowball.theme.ThemeEditing;
import bot.ryuu.snowball.theme.ThemeEmoji;
import bot.ryuu.snowball.theme.ThemeMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public enum Power implements Effect {
    BIG_BAGS("big bags", "Allows you to pick up 3 snowballs at a time") {
        @Override
        public Event action(Player a, Player b, PlayerRepository playerRepository) {
            if (a != null) {
                a.incSnowballAmount(3);
                a.setLastTakeSnowball(LocalDateTime.now());

                playerRepository.save(a);

                PowerSystem.removeActiveObject(a);

                return Event.TAKE_SNOWBALL_BIG_BAGS;
            }

            return super.action(a, b, playerRepository);
        }
    },
    THIEF("thief", "you take half the snowballs from one of the players") {
        @Override
        public Event action(Player a, Player b, PlayerRepository playerRepository) {
            if (a != null && b != null) {
                int factor = b.getSnowballAmount() / 2;

                a.incSnowballAmount(factor);
                a.setLastTakeSnowball(LocalDateTime.now());
                b.incSnowballAmount(-factor);

                playerRepository.saveAll(List.of(a, b));

                PowerSystem.removeActiveObject(a);

                return Event.TAKE_SNOWBALL_THIEF;
            }

            return super.action(a, b, playerRepository);
        }
    },
    FORTUNE("fortune", "increases the chance of hitting a player to 100%") {
        @Override
        public Event action(Player a, Player b, PlayerRepository playerRepository) {
            if (a != null && b != null) {
                a.incSnowballAmount(-1);
                a.incScore(20);

                b.incScore(-5);

                playerRepository.saveAll(List.of(a, b));

                PowerSystem.removeActiveObject(a);

                return Event.HIT;
            }

            return super.action(a, b, playerRepository);
        }
    },
    PACIFIER("pacifier", "useless, but beautiful");

    private final String name;
    private final String description;

    public MessageEmbed info() {
        return ThemeMessage.getMainEmbed()
                .setDescription(ThemeEmoji.POWER.getEmoji().getAsMention() +
                        " you have received the following item: " + ThemeEditing.bold(this.getName()))
                .addField("Description", this.getDescription(), false).build();
    }
}
