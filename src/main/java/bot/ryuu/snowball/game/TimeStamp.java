package bot.ryuu.snowball.game;

import java.time.Duration;
import java.time.LocalDateTime;

public final class TimeStamp {
    public static final int TIMESTAMP_RANDOM_OBJECT_POWER = 5;
    public static final int TIMESTAMP_TAKE_SNOWBALL = 3;

    public static boolean isExecuteRandomObjectPower(LocalDateTime from, LocalDateTime to) {
        return Duration.between(from, to).toMinutes() > TIMESTAMP_RANDOM_OBJECT_POWER;
    }

    public static boolean isExecuteTakeSnowball(LocalDateTime from, LocalDateTime to) {
        return Duration.between(from, to).toMinutes() > TIMESTAMP_TAKE_SNOWBALL;
    }
}
