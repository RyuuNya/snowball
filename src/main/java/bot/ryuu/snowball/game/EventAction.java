package bot.ryuu.snowball.game;

import bot.ryuu.snowball.game.power.Power;
import bot.ryuu.snowball.player.Player;
import bot.ryuu.snowball.player.PlayerRepository;
import bot.ryuu.snowball.theme.ThemeEditing;
import bot.ryuu.snowball.theme.ThemeMessage;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class EventAction {
    public static final int PROBABILITY_FORTUNE = 20;
    public static final int PROBABILITY_BIG_BAGS = 50;
    public static final int PROBABILITY_THIEF = 90;

    public static final int PROBABILITY_SHOT = 60;

    public static PlayerRepository playerRepository;

    public static Event takeSnowball(Player a, Player b) {
        if (PowerSystem.isActiveObject(a)) {
            return PowerSystem.getActiveObject(a).action(a, b, playerRepository);
        } else {
            a.incSnowballAmount(1);
            a.setLastTakeSnowball(LocalDateTime.now());
            playerRepository.save(a);

            return Event.TAKE_SNOWBALL;
        }
    }

    public static Event throwSnowball(Player a, Player b) {
        if (PowerSystem.isActiveObject(a)) {
            return PowerSystem.getActiveObject(a).action(a, b, playerRepository);
        } else {
            a.incSnowballAmount(-1);

            if (randomShot()) {
                a.incScore(25);
                b.incScore(-5);

                playerRepository.saveAll(List.of(a, b));

                return Event.HIT;
            }

            playerRepository.saveAll(List.of(a, b));
        }

        return Event.MISSED;
    }

    public static Power randomPower(Player a) {
        Power power = randomPower();

        a.putObject(power);
        a.setLastRandomObjectPower(LocalDateTime.now());

        playerRepository.save(a);

        return power;
    }

    public static MessageEmbed statisticPlayer(Player player, User user) {
        String statistic =
                "point: " + ThemeEditing.bold(player.getScore()) + "\n" +
                        "level: " + ThemeEditing.bold(player.getLevel()) + "\n" +
                        "snowball: " + ThemeEditing.bold(player.getSnowballAmount());

        return ThemeMessage.getMainEmbed()
                .setTitle("Statistic " + user.getName())
                .setThumbnail(user.getAvatarUrl())
                .setDescription(statistic)
                .build();
    }

    /** Service */
    @Autowired
    private void setPlayerRepository(PlayerRepository playerRepository) {
        EventAction.playerRepository = playerRepository;
    }

    public static boolean randomShot() {
        return Math.ceil(Math.random() * 100) < PROBABILITY_SHOT;
    }

    private static Power randomPower() {
        int random = (int) Math.ceil(Math.random() * 100);

        if (random < PROBABILITY_FORTUNE)
            return Power.FORTUNE;
        else if (random < PROBABILITY_BIG_BAGS)
            return Power.BIG_BAGS;
        else if (random < PROBABILITY_THIEF)
            return Power.THIEF;
        else
            return Power.PACIFIER;
    }
}
