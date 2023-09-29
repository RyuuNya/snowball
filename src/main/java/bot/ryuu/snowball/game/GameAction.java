package bot.ryuu.snowball.game;

import bot.ryuu.snowball.data.DataCluster;
import bot.ryuu.snowball.data.player.Player;
import bot.ryuu.snowball.event.Param;
import bot.ryuu.snowball.event.request.EventRequest;
import bot.ryuu.snowball.event.request.Request;
import bot.ryuu.snowball.event.response.EventResponse;
import bot.ryuu.snowball.event.response.Response;
import bot.ryuu.snowball.game.power.Power;
import bot.ryuu.snowball.game.random.GameRandom;
import bot.ryuu.snowball.tools.Theme;
import net.dv8tion.jda.api.entities.User;

import java.time.LocalDateTime;
import java.util.Optional;

import static bot.ryuu.snowball.event.request.Request.TAKE;

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
            case ACTIVATE -> {
                return activatePower(request);
            }
            case null, default -> {
                return EventResponse.empty();
            }
        }
    }

    private static EventResponse takeSnowball(EventRequest request) {
        Optional<Player> a = request.value("a");
        Optional<DataCluster> cluster = request.value("cluster");

        if (a.isEmpty() || cluster.isEmpty())
            return EventResponse.empty();
        else if (Time.isExecuteTake(a.get().getLastTakeSnowball()))
            return EventResponse.of(Response.TIMER_OVER);
        else if (a.get().isActive())
            return a.get().getActive().action(request);
        else {
            a.get().incSnowball(1)
                    .save(cluster.get());

            return EventResponse.of(Response.TAKE_SNOWBALL);
        }
    }

    private static EventResponse throwSnowball(EventRequest request) {
        Optional<Player> a = request.value("a");
        Optional<Player> b = request.value("b");
        Optional<DataCluster> cluster = request.value("cluster");

        if (a.isEmpty() || b.isEmpty() || cluster.isEmpty())
            return EventResponse.empty();
        else if (a.get().isActive())
            return a.get().getActive().action(request);
        else {
            if (GameRandom.randomShot() && a.get().getSnowball() > 0) {
                a.get().incScore(25)
                        .incSnowball(-1)
                        .save(cluster.get());

                b.get().incScore(-5)
                        .save(cluster.get());

                return EventResponse.of(Response.HIT);
            } else if (a.get().getSnowball() <= 0) {
                return EventResponse.of(Response.THROW_SNOWBALL_LIMIT);
            } else {
                a.get().incSnowball(-1)
                        .save(cluster.get());

                return EventResponse.of(Response.MISSED);
            }
        }
    }

    private static EventResponse randomPower(EventRequest request) {
        Optional<Player> a = request.value("a");
        Optional<DataCluster> cluster = request.value("cluster");

        if (a.isEmpty() || cluster.isEmpty())
            return EventResponse.empty();
        else if (Time.isExecuteRandomPower(a.get().getLastRandomPower()))
            return EventResponse.of(Response.TIMER_OVER);
        else {
            Power power = GameRandom.randomPowerV2();

            a.get().addPower(power)
                    .save(cluster.get());

            return EventResponse.of(
                    Response.RANDOM,
                    new Param("power", power)
            );
        }
    }

    private static EventResponse statsPlayer(EventRequest request) {
        Optional<Player> a = request.value("a");
        Optional<User> user = request.value("user");

        if (a.isEmpty() || user.isEmpty())
            return EventResponse.empty();
        else {
            String statistic =
                    "score: " + a.get().getScore() + "\n" +
                            "snowball: " + a.get().getSnowball();

            return EventResponse.of(
                    Response.STATS,
                    new Param("stats", Theme.main()
                            .setTitle("Statistic " + user.get().getName())
                            .setThumbnail(user.get().getAvatarUrl())
                            .setDescription(statistic)
                            .build())
            );
        }
    }

    private static EventResponse activatePower(EventRequest request) {
        Optional<Player> a = request.value("a");
        Optional<Request> action = request.value("action");
        Optional<DataCluster> cluster = request.value("cluster");
        Power power = Power.valueOf((String) request.value("power").get());

        if (a.isEmpty() || cluster.isEmpty() || action.isEmpty())
            return EventResponse.empty();

        boolean activate = false;

        switch (action.get()) {
            case TAKE -> {
                switch (power) {
                    case FORTUNE, BIG_BAGS, THIEF, BOOST -> activate = true;
                }
            }
            case THROW -> {
                switch (power) {
                    case FORTUNE, ENROLMENT, BOOST, SUPER_THROW -> activate = true;
                }
            }
        }

        if (activate)
            a.get().activate(power)
                    .save(cluster.get());

        return EventResponse.of(
                Response.ACTIVATE,
                new Param("activate", activate)
        );
    }
}