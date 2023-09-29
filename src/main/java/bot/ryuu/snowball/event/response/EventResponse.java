package bot.ryuu.snowball.event.response;

import bot.ryuu.snowball.event.Event;
import bot.ryuu.snowball.event.Param;

import java.util.HashMap;

public class EventResponse extends Event {
    private final Response type;

    private EventResponse(Response type, Param... params) {
        this.body = new HashMap<>();
        this.type = type;

        for (Param param : params)
            body.put(param.name(), param.value());
    }

    private EventResponse(Response type) {
        this.body = new HashMap<>();
        this.type = type;
    }

    public static EventResponse of(Response type, Param... params) {
        return new EventResponse(type, params);
    }

    public static EventResponse of(Response type) {
        return new EventResponse(type);
    }

    public static EventResponse empty() {
        return new EventResponse(Response.NULL);
    }

    public Response type() {
        return type;
    }
}
