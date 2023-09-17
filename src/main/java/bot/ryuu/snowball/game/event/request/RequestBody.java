package bot.ryuu.snowball.game.event.request;

import bot.ryuu.snowball.data.DataCluster;
import bot.ryuu.snowball.data.player.Player;

public record RequestBody(Player a, Player b, DataCluster dataCluster) {
}
