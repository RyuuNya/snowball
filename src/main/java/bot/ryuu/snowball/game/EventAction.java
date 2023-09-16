package bot.ryuu.snowball.game;

import bot.ryuu.snowball.data.DataCluster;
import bot.ryuu.snowball.data.player.Player;
import bot.ryuu.snowball.game.event.Event;
import bot.ryuu.snowball.game.event.EventType;
import bot.ryuu.snowball.game.power.Power;
import bot.ryuu.snowball.theme.Theme;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.time.LocalDateTime;

public class EventAction {
    public static final int PROBABILITY_FORTUNE = 10;
    public static final int PROBABILITY_BIG_BAGS = 40;
    public static final int PROBABILITY_THIEF = 75;
    public static final int PROBABILITY_BOOST = 95;

    public static final int PROBABILITY_SHOT = 60;

    public static <T> Event<T> takeSnowball(Player a, Player b, DataCluster dataCluster) {
        if (TimeStamp.isExecuteTakeSnowball(a.getLastTakeSnowball(), LocalDateTime.now()))
            return Event.of(EventType.TIMER_OVER);
        else if (a.isActive())
            return a.getActive().action(a, b, dataCluster);
        else {
            a.incSnowball(1)
                    .setLastTakeSnowball(LocalDateTime.now())
                    .save(dataCluster.getPlayerRepository());

            return Event.of(EventType.TAKE_SNOWBALL);
        }
    }

    public static <T> Event<T> throwSnowball(Player a, Player b, DataCluster dataCluster) {
        if (a.isActive()) {
            return a.getActive().action(a, b, dataCluster);
        } else {
            if (randomShot() && a.getSnowball() > 0) {
                a.incScore(25)
                        .incSnowball(-1)
                        .save(dataCluster.getPlayerRepository());

                b.incScore(-5)
                        .save(dataCluster.getPlayerRepository());

                return Event.of(EventType.HIT);
            } else if (a.getSnowball() <= 0) {
                return Event.of(EventType.SNOWBALL_LIMIT);
            } else {
                a
                        .incSnowball(-1)
                        .save(dataCluster.getPlayerRepository());

                return Event.of(EventType.MISSED);
            }
        }
    }

    public static Event<Power> randomPower(Player a, DataCluster dataCluster) {
        if (TimeStamp.isExecuteRandomObjectPower(a.getLastRandomPower(), LocalDateTime.now()))
            return Event.of(EventType.TIMER_OVER);

        Power power = randomPower();

        a.addPower(power)
                .setLastRandomPower(LocalDateTime.now())
                .save(dataCluster.getPlayerRepository());

        return Event.of(EventType.NEW_OBJECT, power);
    }

    public static MessageEmbed statisticPlayer(Player player, User user) {
        String statistic =
                "score: " + player.getScore() + "\n" +
                        "snowball: " + player.getSnowball();

        return Theme.getMainEmbed()
                .setTitle("Statistic " + user.getName())
                .setThumbnail(user.getAvatarUrl())
                .setDescription(statistic)
                .build();
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
        else if (random < PROBABILITY_BOOST)
            return Power.BOOST;
        else
            return Power.PACIFIER;
    }
}