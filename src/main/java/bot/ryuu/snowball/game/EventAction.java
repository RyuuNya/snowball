package bot.ryuu.snowball.game;

import bot.ryuu.snowball.data.DataCluster;
import bot.ryuu.snowball.data.player.Player;
import bot.ryuu.snowball.game.event.Event;
import bot.ryuu.snowball.game.event.request.EventRequest;
import bot.ryuu.snowball.game.event.request.Request;
import bot.ryuu.snowball.game.event.request.RequestBody;
import bot.ryuu.snowball.game.event.response.EventResponse;
import bot.ryuu.snowball.game.event.response.Response;
import bot.ryuu.snowball.game.power.Power;
import bot.ryuu.snowball.theme.Theme;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.time.LocalDateTime;

public class EventAction {
    /*
    * pacifier = 3%
    * fortune = 5%
    * enrolment = 10%
    * super throw = 10%
    * thief = 15%
    * boost = 20%
    * big bags = 37%
    *
    * */
    public static final int PROBABILITY_PACIFIER = 3;
    public static final int PROBABILITY_FORTUNE = 8;
    public static final int PROBABILITY_ENROLMENT = 18;
    public static final int PROBABILITY_SUPER_THROW = 28;
    public static final int PROBABILITY_THIEF = 43;
    public static final int PROBABILITY_BOOST = 63;
    public static final int PROBABILITY_BIG_BAGS = 100;

    public static final int PROBABILITY_SHOT = 60;

    public static EventResponse takeSnowball(Player a, Player b, DataCluster dataCluster) {
        if (TimeStamp.isExecuteTakeSnowball(a.getLastTakeSnowball(), LocalDateTime.now()))
            return EventResponse.of(Response.TIMER_OVER);
        else if (a.isActive())
            return a.getActive().action(
                    EventRequest.of(Request.TAKE, new RequestBody(a, b, dataCluster))
            );
        else {
            a.incSnowball(1)
                    .setLastTakeSnowball(LocalDateTime.now())
                    .save(dataCluster.getPlayerRepository());

            return EventResponse.of(Response.TAKE_SNOWBALL);
        }
    }

    public static EventResponse throwSnowball(Player a, Player b, DataCluster dataCluster) {
        if (a.isActive()) {
            return a.getActive().action(
                    EventRequest.of(Request.THROW, new RequestBody(a, b, dataCluster))
            );
        } else {
            if (randomShot() && a.getSnowball() > 0) {
                a.incScore(25)
                        .incSnowball(-1)
                        .save(dataCluster.getPlayerRepository());

                b.incScore(-5)
                        .save(dataCluster.getPlayerRepository());

                return EventResponse.of(Response.HIT);
            } else if (a.getSnowball() <= 0) {
                return EventResponse.of(Response.SNOWBALL_LIMIT);
            } else {
                a
                        .incSnowball(-1)
                        .save(dataCluster.getPlayerRepository());

                return EventResponse.of(Response.MISSED);
            }
        }
    }

    public static EventResponse randomPower(Player a, DataCluster dataCluster) {
        if (TimeStamp.isExecuteRandomObjectPower(a.getLastRandomPower(), LocalDateTime.now()))
            return EventResponse.of(Response.TIMER_OVER);

        Power power = randomPower();

        a.addPower(power)
                .setLastRandomPower(LocalDateTime.now())
                .save(dataCluster.getPlayerRepository());

        return EventResponse.of(Response.NEW_OBJECT, power);
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

    public static boolean activatePower(EventRequest request) {
        Power power = Power.valueOf(request.staticCast());

        System.out.println(" ~ " + power);

        switch (request.getType()) {
            case TAKE -> {
                if (power.equals(Power.FORTUNE) || power.equals(Power.BIG_BAGS) || power.equals(Power.THIEF)) {
                    request.getBody().a().activatePower(power)
                            .save(request.getBody().dataCluster().getPlayerRepository());

                    return true;
                }
            }
            case THROW -> {
                if (power.equals(Power.FORTUNE) || power.equals(Power.BOOST) ||
                        power.equals(Power.ENROLMENT) || power.equals(Power.SUPER_THROW)) {
                    request.getBody().a().activatePower(power)
                            .save(request.getBody().dataCluster().getPlayerRepository());

                    return true;
                }
            }
            case RANDOM ->
            {
                if (power.equals(Power.FORTUNE)) {
                    request.getBody().a().activatePower(power)
                            .save(request.getBody().dataCluster().getPlayerRepository());

                    return true;
                }
            }
        }

        return false;
    }

    public static boolean randomShot() {
        return Math.ceil(Math.random() * 100) < PROBABILITY_SHOT;
    }

    private static Power randomPower() {
        int random = (int) Math.ceil(Math.random() * 100);

        if (random < PROBABILITY_PACIFIER)
            return Power.PACIFIER;
        else if (random < PROBABILITY_FORTUNE)
            return Power.FORTUNE;
        else if (random < PROBABILITY_ENROLMENT)
            return Power.ENROLMENT;
        else if (random < PROBABILITY_SUPER_THROW)
            return Power.SUPER_THROW;
        else if (random < PROBABILITY_THIEF)
            return Power.THIEF;
        else if (random < PROBABILITY_BOOST)
            return Power.BOOST;
        else if (random < PROBABILITY_BIG_BAGS)
            return Power.BIG_BAGS;
        else
            return Power.PACIFIER;
    }
}