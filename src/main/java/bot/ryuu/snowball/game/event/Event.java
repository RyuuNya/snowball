package bot.ryuu.snowball.game.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class Event {
    private EventType type;
    private Object value;

    public Event(EventType type, Object value) {
        this.type = type;
        this.value = value;
    }

    public static Event of(EventType type, Object value) {
        return new Event(type, value);
    }

    public static Event of(EventType type) {
        return new Event(type, null);
    }

    public static Event error() {
        return new Event(EventType.ERROR, null);
    }

    public static Event empty() {
        return new Event(EventType.NULL, null);
    }

    public boolean isPresent() {
        return this.value != null;
    }

    public <T> T staticCast() {
        if (isPresent()) {
            T date = null;
            try {
                date = (T) value;
            } catch (ClassCastException e) {
                System.out.println(e);
            }
            return date;
        } else
            return null;
    }
}
