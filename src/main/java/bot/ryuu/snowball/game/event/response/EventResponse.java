package bot.ryuu.snowball.game.event.response;

import bot.ryuu.snowball.game.event.Event;
import lombok.Getter;

@Getter
public final class EventResponse extends Event {
    private final Response type;

    public EventResponse(Response type, Object value) {
        super(value);

        this.type = type;
    }

    public static EventResponse of(Response type, Object value) {
        return new EventResponse(type, value);
    }

    public static EventResponse of(Response type) {
        return new EventResponse(type, null);
    }

    public static EventResponse empty() {
        return new EventResponse(Response.NULL, null);
    }

    public static EventResponse error() {
        return new EventResponse(Response.ERROR, null);
    }
}
