package bot.ryuu.snowball.game.power;

import bot.ryuu.snowball.data.DataCluster;
import bot.ryuu.snowball.data.player.Player;
import bot.ryuu.snowball.game.EventAction;
import bot.ryuu.snowball.game.event.Event;
import bot.ryuu.snowball.game.event.request.EventRequest;
import bot.ryuu.snowball.game.event.request.Request;
import bot.ryuu.snowball.game.event.response.EventResponse;
import bot.ryuu.snowball.game.event.response.Response;
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
            "boost",
            "https://media.discordapp.net/attachments/1150150954340061254/1150401657847431229/boost.png"
    ) {
        @Override
        public EventResponse action(EventRequest request) {
            Player a = request.getBody().a();
            Player b = request.getBody().b();
            DataCluster dataCluster = request.getBody().dataCluster();

            if (a != null && b != null) {
                if (EventAction.randomShot()) {
                    a.incScore(35)
                            .incSnowball(-1)
                            .removeActive(this)
                            .save(dataCluster.getPlayerRepository());

                    b.incScore(-5)
                            .save(dataCluster.getPlayerRepository());

                    return EventResponse.of(Response.HIT);
                } else {
                    a.incSnowball(-1)
                            .removeActive(this)
                            .save(dataCluster.getPlayerRepository());

                    return EventResponse.of(Response.MISSED);
                }
            }

            return super.action(request);
        }
    },
    BIG_BAGS(
            "Big bags",
            "Allows you to pick up 3 snowballs at a time",
            "big-bags",
            "https://media.discordapp.net/attachments/1150150954340061254/1150401657524453436/big_bags.png"
    ) {
        @Override
        public EventResponse action(EventRequest request) {
            Player a = request.getBody().a();
            DataCluster dataCluster = request.getBody().dataCluster();

            if (a != null) {
                a.incSnowball(3)
                        .setLastTakeSnowball(LocalDateTime.now())
                        .removeActive(this)
                        .save(dataCluster.getPlayerRepository());

                return EventResponse.of(Response.TAKE_SNOWBALL_BIG_BAGS);
            }

            return super.action(request);
        }
    },
    THIEF(
            "Thief",
            "you steal snowballs from the user with the maximum number of snowballs",
            "thief",
            "https://media.discordapp.net/attachments/1150150954340061254/1150422042760466452/thief.png"
    ) {
        @Override
        public EventResponse action(EventRequest request) {
            Player a = request.getBody().a();
            DataCluster dataCluster = request.getBody().dataCluster();

            if (a != null) {
                List<Player> players = dataCluster.getPlayersServer(a.getServer());

                players.sort(Player::compareTo);

                Player b1 = players.get(0);
                int snowball = b1.getSnowball() / 2;

                a.incSnowball(snowball)
                        .setLastTakeSnowball(LocalDateTime.now())
                        .save(dataCluster.getPlayerRepository());

                b1.incSnowball(-snowball)
                        .save(dataCluster.getPlayerRepository());

                return EventResponse.of(Response.TAKE_SNOWBALL_THIEF);
            }

            return super.action(request);
        }
    },
    FORTUNE(
            "Fortune",
            "increases the chance of hitting a player to 100%",
            "fortune",
            "https://media.discordapp.net/attachments/1150150954340061254/1150401658128441354/fortune.png"
    ) {
        @Override
        public EventResponse action(EventRequest request) {
            Player a = request.getBody().a();
            Player b = request.getBody().b();
            DataCluster dataCluster = request.getBody().dataCluster();

            if (request.getType().equals(Request.THROW) && a != null && b != null) {
                if (a.getSnowball() > 0) {
                    a.incSnowball(-1)
                            .incScore(20)
                            .removeActive(this)
                            .save(dataCluster.getPlayerRepository());

                    b.incScore(-5)
                            .save(dataCluster.getPlayerRepository());

                    return EventResponse.of(Response.HIT);
                } else
                    return EventResponse.of(Response.SNOWBALL_LIMIT);
            } else if (request.getType().equals(Request.TAKE) && a != null) {
                a.incSnowball(5)
                        .removeActive(this)
                        .setLastTakeSnowball(LocalDateTime.now())
                        .save(dataCluster.getPlayerRepository());

                return EventResponse.of(Response.TAKE_SNOWBALL_FORTUNE);
            }

            return super.action(request);
        }
    },
    SUPER_THROW(
            "super throw",
            "you throw all your snowballs at multiple users",
            "super-throw",
            "https://media.discordapp.net/attachments/1150150954340061254/1150422914806595604/empty.png"
    ) {
        @Override
        public EventResponse action(EventRequest request) {
            Player a = request.getBody().a();
            Player b = request.getBody().b();
            DataCluster dataCluster = request.getBody().dataCluster();

            if (a != null) {
                List<Player> players = dataCluster.getPlayersServer(a.getServer());
                ArrayList<Player> playersB = new ArrayList<>();

                for (int i = 0; i < a.getSnowball(); i++) {
                    playersB.add(players.get(new Random().nextInt(players.size())));
                }

                a.incScore(25 * a.getSnowball())
                        .incSnowball(-a.getSnowball())
                        .save(dataCluster.getPlayerRepository());

                playersB.forEach(player -> {
                    player.incScore(-5)
                            .save(dataCluster.getPlayerRepository());
                });

                return EventResponse.of(Response.HIT_SUPER_THROW, playersB);
            }

            return super.action(request);
        }
    },
    ENROLMENT(
            "enrolment",
            "You possess a player and throw a snowball at a random player for them",
            "enrolment",
            "https://media.discordapp.net/attachments/1150150954340061254/1150422914806595604/empty.png"
    ) {
        @Override
        public EventResponse action(EventRequest request) {
            Player a = request.getBody().a();
            Player b = request.getBody().b();
            DataCluster dataCluster = request.getBody().dataCluster();

            if (a != null && b != null) {
                List<Player> players = dataCluster.getPlayersServer(a.getServer());
                Player c = players.get(new Random().nextInt(players.size()));



                if (b.getSnowball() > 0) {
                    a.incScore(10)
                                    .save(dataCluster.getPlayerRepository());

                    b.incSnowball(-1)
                            .incScore(25)
                            .save(dataCluster.getPlayerRepository());

                    c.incScore(-10)
                            .save(dataCluster.getPlayerRepository());

                    return EventResponse.of(Response.HIT_ENROLMENT, c);
                } else
                    return EventResponse.of(Response.MISSED_ENROLMENT, c);
            }

            return super.action(request);
        }
    },
    PACIFIER(
            "pacifier",
            "useless, but beautiful",
            "pacifier",
            "https://media.discordapp.net/attachments/1150150954340061254/1150422914806595604/empty.png"
    );

    private final String name;
    private final String description;

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