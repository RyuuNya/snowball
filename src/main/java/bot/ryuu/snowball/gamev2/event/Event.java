package bot.ryuu.snowball.gamev2.event;

import java.util.HashMap;

public class Event {
    protected HashMap<String, Object> body;

    public <T> T value(String value) {
        Object object = body.get(value);

        if (object != null) {
            T date = null;

            try {
                date = (T) object;
            } catch (ClassCastException e) {
                System.out.println(e);
            }

            return date;
        } else
            return null;
    }
}
