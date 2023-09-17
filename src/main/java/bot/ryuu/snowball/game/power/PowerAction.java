package bot.ryuu.snowball.game.power;

import bot.ryuu.snowball.data.DataCluster;
import bot.ryuu.snowball.data.player.Player;
import bot.ryuu.snowball.data.player.PlayerRepository;
import bot.ryuu.snowball.game.event.Event;

public interface PowerAction {
    default Event action(Player a, Player b, DataCluster dataCluster) {
        return Event.empty();
    }
}