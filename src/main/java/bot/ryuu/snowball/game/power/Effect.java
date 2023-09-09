package bot.ryuu.snowball.game.power;

import bot.ryuu.snowball.game.Event;
import bot.ryuu.snowball.player.Player;
import bot.ryuu.snowball.player.PlayerRepository;

public interface Effect {
    default Event action(Player a, Player b, PlayerRepository playerRepository) {
        return Event.NULL;
    }
}
