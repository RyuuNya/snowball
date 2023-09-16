package bot.ryuu.snowball.game.power;

import bot.ryuu.snowball.data.DataCluster;
import bot.ryuu.snowball.data.player.Player;
import bot.ryuu.snowball.game.EventAction;
import bot.ryuu.snowball.game.event.Event;
import bot.ryuu.snowball.game.event.EventType;
import bot.ryuu.snowball.language.Language;
import bot.ryuu.snowball.theme.Theme;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

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
        public <T> Event<T> action(Player a, Player b, DataCluster dataCluster) {
            if (a != null && b != null) {
                if (EventAction.randomShot()) {
                    a.incScore(35)
                            .incSnowball(-1)
                            .removeActive(this)
                            .save(dataCluster.getPlayerRepository());

                    b.incScore(-5)
                            .save(dataCluster.getPlayerRepository());

                    return Event.of(EventType.HIT);
                } else {
                    a.incSnowball(-1)
                            .removeActive(this)
                            .save(dataCluster.getPlayerRepository());

                    return Event.of(EventType.MISSED);
                }
            }

            return super.action(a, b, dataCluster);
        }
    },
    BIG_BAGS(
            "Big bags",
            "Allows you to pick up 3 snowballs at a time",
            "big-bags",
            "https://media.discordapp.net/attachments/1150150954340061254/1150401657524453436/big_bags.png"
    ) {
        @Override
        public <T> Event<T> action(Player a, Player b, DataCluster dataCluster) {
            if (a != null) {
                a.incSnowball(3)
                        .setLastTakeSnowball(LocalDateTime.now())
                        .removeActive(this)
                        .save(dataCluster.getPlayerRepository());

                return Event.of(EventType.TAKE_SNOWBALL_BIG_BAGS);
            }

            return super.action(a, b, dataCluster);
        }
    },
    THIEF(
            "Thief",
            "you steal snowballs from the user with the maximum number of snowballs",
            "thief",
            "https://media.discordapp.net/attachments/1150150954340061254/1150422042760466452/thief.png"
    ) {
        @Override
        public <T> Event<T> action(Player a, Player b, DataCluster dataCluster) {
            if (a != null) {
                List<Player> players = dataCluster.getPlayersServer(a.getServer());

                players.sort(Player::compareTo);

                Player b1 = players.get(0);
                int snowball = b1.getSnowball() / 2;

                a.incSnowball(snowball)
                        .save(dataCluster.getPlayerRepository());

                b1.incSnowball(-snowball)
                        .save(dataCluster.getPlayerRepository());
            }

            return super.action(a, b, dataCluster);
        }
    },
    FORTUNE(
            "Fortune",
            "increases the chance of hitting a player to 100%",
            "fortune",
            "https://media.discordapp.net/attachments/1150150954340061254/1150401658128441354/fortune.png"
    ) {
        @Override
        public <T> Event<T> action(Player a, Player b, DataCluster dataCluster) {
            if (a != null && b != null) {
                a.incSnowball(-1)
                        .incScore(20)
                        .removeActive(this)
                        .save(dataCluster.getPlayerRepository());

                b.incScore(-5)
                        .save(dataCluster.getPlayerRepository());

                return Event.of(EventType.HIT);
            }

            return super.action(a, b, dataCluster);
        }
    },
    SUPER_THROW(
            "super throw",
            "you throw all your snowballs at multiple users",
            "super-throw",
            "-"
    ),
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