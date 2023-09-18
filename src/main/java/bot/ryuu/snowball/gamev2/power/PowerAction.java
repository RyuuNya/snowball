package bot.ryuu.snowball.gamev2.power;

import bot.ryuu.snowball.gamev2.event.request.EventRequest;
import bot.ryuu.snowball.gamev2.event.response.EventResponse;

public interface PowerAction {
    default EventResponse action(EventRequest request) {
        return EventResponse.empty();
    }
}