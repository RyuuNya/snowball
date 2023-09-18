package bot.ryuu.snowball.gamev2;

import bot.ryuu.snowball.data.DataCluster;
import bot.ryuu.snowball.data.player.Player;
import bot.ryuu.snowball.gamev2.event.Param;
import bot.ryuu.snowball.gamev2.event.request.EventRequest;
import bot.ryuu.snowball.gamev2.event.response.EventResponse;
import bot.ryuu.snowball.gamev2.event.response.Response;
import bot.ryuu.snowball.gamev2.power.Power;
import bot.ryuu.snowball.theme.Theme;
import net.dv8tion.jda.api.entities.User;

import java.time.LocalDateTime;

public interface GameAction {
    static EventResponse execute(EventRequest request) {
        switch (request.type()) {
            case TAKE -> {
                return takeSnowball(request);
            }
            case THROW -> {
                return throwSnowball(request);
            }
            case RANDOM -> {
                return randomPower(request);
            }
            case STATS -> {
                return statsPlayer(request);
            }
            default -> {
                return EventResponse.empty();
            }
        }
    }

    private static EventResponse takeSnowball(EventRequest request) {
        DataCluster cluster = request.value("cluster");
        Player a = request.value("a");

        if (a == null || cluster == null)
            return EventResponse.empty();
        else if (Time.isExecuteTake(a.getLastTakeSnowball(), LocalDateTime.now()))
            return EventResponse.of(Response.TIMER_OVER);
        else if (a.isActive())
            return a.getActive().action(request);
        else {
            a.incSnowball(1)
                    .setLastTakeSnowball(LocalDateTime.now())
                    .save(cluster.getPlayerRepository());

            return EventResponse.of(Response.TAKE_SNOWBALL);
        }
    }

    private static EventResponse throwSnowball(EventRequest request) {
        Player a = request.value("a");
        Player b = request.value("b");
        DataCluster cluster = request.value("cluster");

        if (a == null || b == null || cluster == null)
            return EventResponse.empty();
        else if (a.isActive())
            return a.getActive().action(request);
        else {
            if (GameRandom.randomShot() && a.getSnowball() > 0) {
                a.incScore(25)
                        .incSnowball(-1)
                        .save(cluster.getPlayerRepository());

                b.incScore(-5)
                        .save(cluster.getPlayerRepository());

                return EventResponse.of(Response.HIT);
            } else if (a.getSnowball() <= 0)
                return EventResponse.of(Response.SNOWBALL_LIMIT);
            else {
                a
                        .incSnowball(-1)
                        .save(cluster.getPlayerRepository());

                return EventResponse.of(Response.MISSED);
            }
        }
    }

    private static EventResponse randomPower(EventRequest request) {
        Player a = request.value("a");
        DataCluster cluster = request.value("cluster");

        if (a == null || cluster == null)
            return EventResponse.empty();
        else if (Time.isExecuteRandomPower(a.getLastRandomPower(), LocalDateTime.now()))
            return EventResponse.of(Response.TIMER_OVER);
        else {
            Power power = GameRandom.randomPowerV2();

            a.addPower(power)
                    .setLastRandomPower(LocalDateTime.now())
                    .save(cluster.getPlayerRepository());

            return EventResponse.of(
                    Response.NEW_OBJECT,
                    new Param("power", power)
            );
        }
    }

    private static EventResponse statsPlayer(EventRequest request) {
        Player a = request.value("a");
        User user = request.value("user");

        if (a == null || user == null)
            return EventResponse.empty();
        else {
            String statistic =
                    "score: " + a.getScore() + "\n" +
                            "snowball: " + a.getSnowball();

            return EventResponse.of(
                    Response.STATS,
                    new Param("stats", Theme.getMainEmbed()
                            .setTitle("Statistic " + user.getName())
                            .setThumbnail(user.getAvatarUrl())
                            .setDescription(statistic)
                            .build())
            );
        }
    }

    private static EventResponse activatePower(EventRequest request) {
        Player a = request.value("a");
        DataCluster cluster = request.value("cluster");
        Power power = Power.valueOf(request.value("power"));

        if (a == null || cluster == null)
            return EventResponse.empty();

        boolean activate = false;

        switch (request.type()) {
            case TAKE -> activate = power.equals(Power.FORTUNE) || power.equals(Power.BIG_BAGS)
                    || power.equals(Power.THIEF);
            case THROW -> activate = power.equals(Power.FORTUNE) || power.equals(Power.BOOST)
                    || power.equals(Power.ENROLMENT) || power.equals(Power.SUPER_THROW);
        }

        if (activate)
            a.activatePower(power)
                    .save(cluster.getPlayerRepository());

        return EventResponse.of(
                Response.ACTIVATE,
                new Param("activate", activate)
        );
    }
}
