package bot.ryuu.snowball.game.power;

import bot.ryuu.snowball.data.DataCluster;
import bot.ryuu.snowball.data.player.Player;
import bot.ryuu.snowball.data.player.PlayerRepository;
import bot.ryuu.snowball.game.event.Event;
import bot.ryuu.snowball.game.event.request.EventRequest;
import bot.ryuu.snowball.game.event.response.EventResponse;

public interface PowerAction {
    default EventResponse action(EventRequest request) {
        return EventResponse.empty();
    }
}