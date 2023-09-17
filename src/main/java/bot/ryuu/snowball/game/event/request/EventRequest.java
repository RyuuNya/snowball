package bot.ryuu.snowball.game.event.request;

import bot.ryuu.snowball.game.event.Event;
import lombok.Getter;

@Getter
public final class EventRequest extends Event {
    private final Request type;

    private final RequestBody body;

    private EventRequest(Request type, RequestBody body, Object value) {
        super(value);

        this.type = type;
        this.body = body;
    }

    public static EventRequest of(Request type, RequestBody body, Object value) {
        return new EventRequest(type, body, value);
    }

    public static EventRequest of(Request type, RequestBody body) {
        return new EventRequest(type, body, null);
    }

    public static EventRequest empty() {
        return new EventRequest(Request.NULL, null, null);
    }

    public static EventRequest error() {
        return new EventRequest(Request.ERROR, null, null);
    }
}