package bot.ryuu.snowball.game;

import java.time.Duration;
import java.time.LocalDateTime;

public interface TimeStamp {
    int TIMESTAMP_RANDOM_POWER = 10;
    int TIMESTAMP_TAKE_SNOWBALL = 3;

    static boolean isExecuteRandomObjectPower(LocalDateTime from, LocalDateTime to) {
        return Duration.between(from, to).toMinutes() < TIMESTAMP_RANDOM_POWER;
    }

    static boolean isExecuteTakeSnowball(LocalDateTime from, LocalDateTime to) {
        return Duration.between(from, to).toMinutes() < TIMESTAMP_TAKE_SNOWBALL;
    }
}
