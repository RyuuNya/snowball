package bot.ryuu.snowball.player;

import bot.ryuu.snowball.game.power.Power;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Player implements Comparable<Player> {
    @Id
    private String id;

    private String idMember;

    public int score;
    private int level;
    private int snowballAmount;

    private String server;

    private LocalDateTime lastRandomObjectPower;
    private LocalDateTime lastTakeSnowball;

    private Set<Power> objectPowerSet;

    public void putObject(Power power) {
        this.objectPowerSet.add(power);
    }

    public boolean containObject(Power power) {
        return objectPowerSet.contains(power);
    }

    public void removeObject(Power power) {
        this.objectPowerSet.remove(power);
    }

    public Player incSnowballAmount(int increment) {
        setSnowballAmount(getSnowballAmount() + increment);
        return this;
    }

    public Player incScore(int increment) {
        setScore(getScore() + increment);
        return this;
    }

    @Override
    public int compareTo(@NotNull Player o) {
        return (this.score < o.getScore()) ? 1 : -1;
    }
}