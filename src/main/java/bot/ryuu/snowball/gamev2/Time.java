package bot.ryuu.snowball.gamev2;

import java.time.Duration;
import java.time.LocalDateTime;

public interface Time {
    int TIMESTAMP_RANDOM_POWER = 5;
    int TIMESTAMP_TAKE_SNOWBALL = 2;

    static boolean isExecuteRandomPower(LocalDateTime from, LocalDateTime to) {
        return Duration.between(from, to).toMinutes() < TIMESTAMP_RANDOM_POWER;
    }

    static boolean isExecuteTake(LocalDateTime from, LocalDateTime to) {
        return Duration.between(from, to).toMinutes() < TIMESTAMP_TAKE_SNOWBALL;
    }
}
