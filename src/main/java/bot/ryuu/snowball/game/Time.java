package bot.ryuu.snowball.game;

import java.time.Duration;
import java.time.LocalDateTime;

public interface Time {
    int TIMESTAMP_RANDOM_POWER = 5;
    int TIMESTAMP_TAKE_SNOWBALL = 2;

    static boolean isExecuteRandomPower(LocalDateTime from) {
        return Duration.between(from, LocalDateTime.now()).toMinutes() < TIMESTAMP_RANDOM_POWER;
    }

    static boolean isExecuteTake(LocalDateTime from) {
        return Duration.between(from, LocalDateTime.now()).toMinutes() < TIMESTAMP_TAKE_SNOWBALL;
    }
}
