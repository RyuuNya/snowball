package bot.ryuu.snowball.event;

import java.util.HashMap;
import java.util.Optional;

public class Event {
    protected HashMap<String, Object> body;

    public <T> Optional<T> value(String value) {
        Object object = body.get(value);

        if (object != null) {
            T date = null;

            try {
                date = (T) object;
            } catch (ClassCastException e) {
                e.printStackTrace();
            }

            return Optional.of(date);
        } else
            return Optional.empty();
    }

    public <T> T valueNoOptional(String value) {
        Object object = body.get(value);

        if (object != null) {
            T date = null;

            try {
                date = (T) object;
            } catch (ClassCastException e) {
                e.printStackTrace();
            }

            return date;
        } else
            return null;
    }
}