package bot.ryuu.snowball.gamev2.power;

import bot.ryuu.snowball.data.DataCluster;
import bot.ryuu.snowball.data.player.Player;
import bot.ryuu.snowball.gamev2.GameRandom;
import bot.ryuu.snowball.gamev2.event.Param;
import bot.ryuu.snowball.gamev2.event.request.EventRequest;
import bot.ryuu.snowball.gamev2.event.request.Request;
import bot.ryuu.snowball.gamev2.event.response.EventResponse;
import bot.ryuu.snowball.gamev2.event.response.Response;
import bot.ryuu.snowball.language.Language;
import bot.ryuu.snowball.theme.Theme;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@AllArgsConstructor
public enum Power implements PowerAction {
    BOOST(
            "Boost",
            "You get more points for hitting a player",
            PowerRarity.RARE,
            "boost",
            "https://media.discordapp.net/attachments/1150150954340061254/1150401657847431229/boost.png"
    ) {
        @Override
        public EventResponse action(EventRequest request) {
            Player a = request.value("a");
            Player b = request.value("b");
            DataCluster cluster = request.value("cluster");

            if (a != null && b != null && cluster != null) {
                if (GameRandom.randomShot()) {
                    a.incScore(35)
                            .incSnowball(-1)
                            .removeActive(this)
                            .save(cluster.getPlayerRepository());

                    b.incScore(-5)
                            .save(cluster.getPlayerRepository());

                    return EventResponse.of(Response.HIT);
                } else {
                    a.incSnowball(-1)
                            .removeActive(this)
                            .save(cluster.getPlayerRepository());

                    return EventResponse.of(Response.MISSED);
                }
            }

            return super.action(request);
        }
    },
    BIG_BAGS(
            "Big bags",
            "Allows you to pick up 3 snowballs at a time",
            PowerRarity.USUAL,
            "big-bags",
            "https://media.discordapp.net/attachments/1150150954340061254/1150401657524453436/big_bags.png"
    ) {
        @Override
        public EventResponse action(EventRequest request) {
            Player a = request.value("a");
            DataCluster cluster = request.value("cluster");

            if (a != null && cluster != null) {
                a.incSnowball(3)
                        .setLastTakeSnowball(LocalDateTime.now())
                        .removeActive(this)
                        .save(cluster.getPlayerRepository());

                return EventResponse.of(Response.TAKE_SNOWBALL_BIG_BAGS);
            }

            return super.action(request);
        }
    },
    THIEF(
            "Thief",
            "you steal snowballs from the user with the maximum number of snowballs",
            PowerRarity.USUAL,
            "thief",
            "https://media.discordapp.net/attachments/1150150954340061254/1150422042760466452/thief.png"
    ) {
        @Override
        public EventResponse action(EventRequest request) {
            Player a = request.value("a");
            DataCluster cluster = request.value("cluster");


            if (a != null && cluster != null) {
                List<Player> players = cluster.getPlayersServer(a.getServer());

                players.sort(Player::compareTo);

                Player b1 = players.get(0);
                int snowball = b1.getSnowball() / 2;

                a.incSnowball(snowball)
                        .setLastTakeSnowball(LocalDateTime.now())
                        .save(cluster.getPlayerRepository());

                b1.incSnowball(-snowball)
                        .save(cluster.getPlayerRepository());

                return EventResponse.of(Response.TAKE_SNOWBALL_THIEF);
            }

            return super.action(request);
        }
    },
    FORTUNE(
            "Fortune",
            "increases the chance of hitting a player to 100%",
            PowerRarity.LEGENDARY,
            "fortune",
            "https://media.discordapp.net/attachments/1150150954340061254/1150401658128441354/fortune.png"
    ) {
        @Override
        public EventResponse action(EventRequest request) {
            Player a = request.value("a");
            Player b = request.value("b");
            DataCluster cluster = request.value("cluster");

            if (request.type().equals(Request.THROW) && a != null && b != null && cluster != null) {
                if (a.getSnowball() > 0) {
                    a.incSnowball(-1)
                            .incScore(20)
                            .removeActive(this)
                            .save(cluster.getPlayerRepository());

                    b.incScore(-5)
                            .save(cluster.getPlayerRepository());

                    return EventResponse.of(Response.HIT);
                } else
                    return EventResponse.of(Response.THROW_SNOWBALL_LIMIT);
            } else if (request.type().equals(Request.TAKE) && a != null && cluster != null) {
                a.incSnowball(5)
                        .removeActive(this)
                        .setLastTakeSnowball(LocalDateTime.now())
                        .save(cluster.getPlayerRepository());

                return EventResponse.of(Response.TAKE_SNOWBALL_FORTUNE);
            }

            return super.action(request);
        }
    },
    SUPER_THROW(
            "super throw",
            "you throw all your snowballs at multiple users",
            PowerRarity.RARE,
            "super-throw",
            "https://media.discordapp.net/attachments/1150150954340061254/1150422914806595604/empty.png"
    ) {
        @Override
        public EventResponse action(EventRequest request) {
            Player a = request.value("a");
            DataCluster cluster = request.value("cluster");

            if (a != null && cluster != null) {
                List<Player> players = cluster.getPlayersServer(a.getServer());
                ArrayList<Player> playersB = new ArrayList<>();

                for (int i = 0; i < a.getSnowball(); i++) {
                    playersB.add(players.get(new Random().nextInt(players.size())));
                }

                a.incScore(25 * a.getSnowball())
                        .incSnowball(-a.getSnowball())
                        .save(cluster.getPlayerRepository());

                playersB.forEach(player -> {
                    player.incScore(-5)
                            .save(cluster.getPlayerRepository());
                });

                return EventResponse.of(
                        Response.HIT_SUPER_THROW,
                        new Param("players", playersB)
                );
            }

            return super.action(request);
        }
    },
    ENROLMENT(
            "enrolment",
            "You possess a player and throw a snowball at a random player for them",
            PowerRarity.LEGENDARY,
            "enrolment",
            "https://media.discordapp.net/attachments/1150150954340061254/1150422914806595604/empty.png"
    ) {
        @Override
        public EventResponse action(EventRequest request) {
            Player a = request.value("a");
            Player b = request.value("b");
            DataCluster cluster = request.value("cluster");

            if (a != null && b != null && cluster != null) {
                List<Player> players = cluster.getPlayersServer(a.getServer());
                Player c = players.get(new Random().nextInt(players.size()));

                if (b.getSnowball() > 0) {
                    a.incScore(10)
                                    .save(cluster.getPlayerRepository());

                    b.incSnowball(-1)
                            .incScore(25)
                            .save(cluster.getPlayerRepository());

                    c.incScore(-10)
                            .save(cluster.getPlayerRepository());

                    return EventResponse.of(
                            Response.HIT_ENROLMENT,
                            new Param("c", c)
                    );
                } else
                    return EventResponse.of(
                            Response.MISSED_ENROLMENT,
                            new Param("c", c)
                    );
            }

            return super.action(request);
        }
    },
    PACIFIER(
            "pacifier",
            "useless, but beautiful",
            PowerRarity.LEGENDARY,
            "pacifier",
            "https://media.discordapp.net/attachments/1150150954340061254/1150422914806595604/empty.png"
    );

    private final String name;
    private final String description;

    private final PowerRarity rarity;
    private final String descriptionUrl;
    private final String imgUrl;

    public MessageEmbed infoLanguage(String id, String lang) {
        String fieldTitle = (lang.equals("en")) ? "Description" : "Описание";

        return Theme.getMainEmbed()
                .setDescription(
                        "<@" + id + "> " +
                                Language.message("give-item", lang) + getName()
                )
                .setThumbnail(getImgUrl())
                .addField(
                        fieldTitle, Language.message(this.getDescriptionUrl(), lang), false
                ).build();
    }

    public MessageEmbed infoCommandLanguage(String lang) {
        String fieldTitle = (lang.equals("en")) ? "Description" : "Описание";

        return Theme.getMainEmbed()
                .setDescription("### " + getName())
                .addField(fieldTitle, Language.message(this.getDescriptionUrl(), lang), false)
                .setThumbnail(this.getImgUrl()).build();
    }
}