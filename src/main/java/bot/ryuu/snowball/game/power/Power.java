package bot.ryuu.snowball.game.power;

import bot.ryuu.snowball.data.DataCluster;
import bot.ryuu.snowball.data.player.Player;
import bot.ryuu.snowball.event.Event;
import bot.ryuu.snowball.event.Param;
import bot.ryuu.snowball.event.request.EventRequest;
import bot.ryuu.snowball.event.response.EventResponse;
import bot.ryuu.snowball.event.response.Response;
import bot.ryuu.snowball.game.random.GameRandom;
import bot.ryuu.snowball.game.random.Rarity;
import bot.ryuu.snowball.tools.Theme;
import bot.ryuu.snowball.tools.language.Language;
import bot.ryuu.snowball.tools.language.Messages;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.dv8tion.jda.api.entities.MessageEmbed;

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@AllArgsConstructor
public enum Power implements PowerAction {
    BOOST(
            "Boost",
            """
                    Your strength and power increases several times over:
                      When picking up snowballs, time is ignored when picking up snowballs.
                      When throwing a snowball, you get more points for hitting it
                    """,
            Rarity.RARE,
            "BOOST",
            "https://media.discordapp.net/attachments/1150150954340061254/1150401657847431229/boost.png"
    ) {
        @Override
        public EventResponse action(EventRequest request) {
            Optional<Player> a = request.value("a");
            Optional<Player> b = request.value("b");
            Optional<DataCluster> cluster = request.value("cluster");

            switch (request.type()) {
                case TAKE -> {
                    if (a.isPresent() && cluster.isPresent()) {
                        a.get().incSnowball(1)
                                .setLastTakeSnowball(LocalDateTime.now().minusMinutes(15))
                                .deactivate()
                                .save(cluster.get());

                        return EventResponse.of(Response.TAKE_SNOWBALL_BOOST);
                    }
                }
                case THROW -> {
                    if (a.isPresent() && b.isPresent() && cluster.isPresent()) {
                        if (GameRandom.randomShot()) {
                            a.get().incScore(35)
                                    .incSnowball(-1)
                                    .deactivate()
                                    .save(cluster.get());

                            b.get().incScore(-5)
                                    .save(cluster.get());

                            return EventResponse.of(Response.HIT);
                        } else {
                            a.get().incSnowball(-1)
                                    .deactivate()
                                    .save(cluster.get());

                            return EventResponse.of(Response.MISSED);
                        }
                    }
                }
            }

            return super.action(request);
        }
    },
    BIG_BAGS(
            "Big bags",
            "Allows you to pick up 3 snowballs at a time",
            Rarity.USUAL,
            "BIG_BAGS",
            "https://media.discordapp.net/attachments/1150150954340061254/1150401657524453436/big_bags.png"
    ) {
        @Override
        public EventResponse action(EventRequest request) {
            Optional<Player> a = request.value("a");
            Optional<DataCluster> cluster = request.value("cluster");

            if (a.isPresent() && cluster.isPresent()) {
                a.get().incSnowball(3)
                        .deactivate()
                        .save(cluster.get());

                return EventResponse.of(Response.TAKE_SNOWBALL_BIG_BAGS);
            }

            return super.action(request);
        }
    },
    THIEF(
            "Thief",
            "You steal half of the snowballs from the player with the most snowballs",
            Rarity.USUAL,
            "THIEF",
            "https://media.discordapp.net/attachments/1150150954340061254/1150422042760466452/thief.png"
    ) {
        @Override
        public EventResponse action(EventRequest request) {
            Optional<Player> a = request.value("a");
            Optional<DataCluster> cluster = request.value("cluster");

            if (a.isPresent() && cluster.isPresent()) {
                List<Player> players = cluster.get().getPlayers(a.get().getServer());

                players.sort(Player::compareTo);

                Player b = players.getFirst();
                int snowball = b.getSnowball() / 2;

                a.get().incSnowball(snowball)
                        .deactivate()
                        .save(cluster.get());

                b.incSnowball(-snowball)
                        .save(cluster.get());

                return EventResponse.of(Response.TAKE_SNOWBALL_THIEF);
            }

            return super.action(request);
        }
    },
    FORTUNE(
            "Fortune",
            """
                    Luck is on your side:
                      When you pick up snowballs, you collect 5 snowballs
                      When throwing snowballs, you get a 100% chance of hitting them
                    """,
            Rarity.LEGENDARY,
            "FORTUNE",
            "https://media.discordapp.net/attachments/1150150954340061254/1150401658128441354/fortune.png"
    ) {
        @Override
        public EventResponse action(EventRequest request) {
            Optional<Player> a = request.value("a");
            Optional<Player> b = request.value("b");
            Optional<DataCluster> cluster = request.value("cluster");

            switch (request.type()) {
                case TAKE -> {
                    if (a.isPresent() && cluster.isPresent()) {
                        a.get().incSnowball(5)
                                .deactivate()
                                .save(cluster.get());

                        return EventResponse.of(Response.TAKE_SNOWBALL_FORTUNE);
                    }
                }
                case THROW -> {
                    if (a.isPresent() && b.isPresent() && cluster.isPresent()) {
                        a.get().incScore(25)
                                .incSnowball(-1)
                                .deactivate()
                                .save(cluster.get());

                        b.get().incScore(-10)
                                .save(cluster.get());

                        return EventResponse.of(Response.HIT);
                    }
                }
            }

            return super.action(request);
        }
    },
    SUPER_THROW(
            "Super throw",
            "You throw all your snowballs at multiple players",
            Rarity.RARE,
            "SUPER_THROW",
            "https://media.discordapp.net/attachments/1150150954340061254/1150422914806595604/empty.png"
    ) {
        @Override
        public EventResponse action(EventRequest request) {
            Optional<Player> a = request.value("a");
            Optional<DataCluster> cluster = request.value("cluster");

            if (a.isPresent() && cluster.isPresent()) {
                List<Player> players = cluster.get().getPlayers(a.get().getServer());
                Set<Player> hitting = new HashSet<>();

                for (int i = 0; i < a.get().getSnowball(); i++) {
                    if (GameRandom.randomShot()) {
                        Player b = players.get(new Random().nextInt(players.size()));
                        b.incScore(-15)
                                .save(cluster.get());

                        hitting.add(b);
                    }
                }

                a.get().incScore(25 * a.get().getSnowball())
                        .incSnowball(-1)
                        .deactivate()
                        .save(cluster.get());

                return EventResponse.of(
                        Response.HIT_SUPER_THROW,
                        new Param("hitting", hitting)
                );
            }

            return super.action(request);
        }
    },
    ENROLMENT(
            "Enrolment",
            "You take over the player and throw a snowball for them",
            Rarity.LEGENDARY,
            "ENROLMENT",
            "https://media.discordapp.net/attachments/1150150954340061254/1150422914806595604/empty.png"
    ) {
        @Override
        public EventResponse action(EventRequest request) {
            Optional<Player> a = request.value("a");
            Optional<Player> b = request.value("b");
            Optional<DataCluster> cluster = request.value("cluster");

            if (a.isPresent() && b.isPresent() && cluster.isPresent()) {
                List<Player> players = cluster.get().getPlayers(a.get().getServer());
                Player c = players.get(new Random().nextInt(players.size()));

                if (b.get().getSnowball() > 0) {
                    a.get().incScore(10)
                            .deactivate()
                            .save(cluster.get());

                    b.get().incScore(25)
                            .incSnowball(-1)
                            .save(cluster.get());

                    c.incScore(-15)
                            .save(cluster.get());

                    return EventResponse.of(Response.HIT_ENROLMENT);
                } else
                    return EventResponse.of(Response.MISSED_ENROLMENT);
            }

            return super.action(request);
        }
    },
    PACIFIER(
            "Pacifier",
            "Useless, but beautiful",
            Rarity.LEGENDARY,
            "PACIFIER",
            "https://media.discordapp.net/attachments/1150150954340061254/1150422914806595604/empty.png"
    );

    private final String name;
    private final String description;

    private final Rarity rarity;

    private final String description_url;
    private final String image_url;

    public MessageEmbed give(String id, Language lang) {
        String fieldTitle = (lang == Language.EN) ? "Description" : "Описание";

        return Theme.main()
                .setDescription(
                        "<@" + id + "> " +
                                Messages.message("GIVE_POWER", lang) + getName()
                )
                .setThumbnail(getImage_url())
                .addField(
                        fieldTitle, Messages.message(this.getDescription_url(), lang), false
                ).build();
    }

    public MessageEmbed info(Language lang) {
        String fieldTitle = (lang == Language.EN) ? "Description" : "Описание";

        return Theme.main()
                .setDescription("### " + getName())
                .addField(fieldTitle, Messages.message(this.getDescription_url(), lang), false)
                .setThumbnail(this.getImage_url()).build();
    }
}