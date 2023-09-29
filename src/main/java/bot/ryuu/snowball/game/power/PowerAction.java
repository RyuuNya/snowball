package bot.ryuu.snowball.game.power;

import bot.ryuu.snowball.event.request.EventRequest;
import bot.ryuu.snowball.event.response.EventResponse;

public interface PowerAction {
    default EventResponse action(EventRequest request) {
        return EventResponse.empty();
    }
}
