package bot.ryuu.snowball.game.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Event {
    private Object value;

    public Event(Object value) {
        this.value = value;
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
