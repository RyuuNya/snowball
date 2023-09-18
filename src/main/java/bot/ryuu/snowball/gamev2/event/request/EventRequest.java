package bot.ryuu.snowball.gamev2.event.request;

import bot.ryuu.snowball.gamev2.event.Event;
import bot.ryuu.snowball.gamev2.event.Param;

import java.util.HashMap;

public final class EventRequest extends Event {
    private final Request type;

    private EventRequest(Request type, Param... params) {
        this.body = new HashMap<>();
        this.type = type;

        for (Param param : params)
            body.put(param.name(), param.value());
    }

    private EventRequest(Request type) {
        this.body = new HashMap<>();
        this.type = type;
    }

    public static EventRequest of(Request type, Param... params) {
        return new EventRequest(type, params);
    }

    public static EventRequest of(Request type) {
        return new EventRequest(type);
    }

    public static EventRequest empty() {
        return new EventRequest(Request.NULL);
    }

    public Request type() {
        return type;
    }
}