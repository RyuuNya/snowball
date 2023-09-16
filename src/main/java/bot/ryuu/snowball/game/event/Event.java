package bot.ryuu.snowball.game.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class Event<T> {
    private EventType type;
    private T value;

    public Event(EventType type, T value) {
        this.type = type;
        this.value = value;
    }

    public static <T> Event<T> of(EventType type, T value) {
        return new Event<>(type, value);
    }

    public static <T> Event<T> of(EventType type) {
        return new Event<>(type, null);
    }

    public static <T> Event<T> error() {
        return new Event<>(EventType.ERROR, null);
    }

    public static <T> Event<T> empty() {
        return new Event<>(EventType.NULL, null);
    }

    public boolean isPresent() {
        return this.value != null;
    }
}
