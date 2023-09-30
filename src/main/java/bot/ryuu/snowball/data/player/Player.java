package bot.ryuu.snowball.data.player;

import bot.ryuu.snowball.data.DataCluster;
import bot.ryuu.snowball.game.power.Power;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Player implements Comparable<Player> {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String member;
    private String server;

    private int snowball;
    private int score;

    private LocalDateTime lastTakeSnowball;
    private LocalDateTime lastRandomPower;

    private Set<Power> powers;
    private Power active;

    public Player addPower(Power power) {
        this.lastRandomPower = LocalDateTime.now();
        this.powers.add(power);
        return this;
    }

    public Player removePower(Power power) {
        this.powers.remove(power);
        return this;
    }

    public boolean containPower(Power power) {
        return this.powers.contains(power);
    }

    public boolean isActive() {
        return this.active != null;
    }

    public Player activate(Power power) {
        if (this.powers.contains(power))
            this.active = power;
        this.powers.remove(power);
        return this;
    }

    public Player deactivate() {
        this.active = null;
        return this;
    }

    public Player incSnowball(int increment) {
        if (increment > 0)
            this.lastTakeSnowball = LocalDateTime.now();
        this.snowball += increment;
        return this;
    }

    public Player incScore(int increment) {
        this.score += increment;
        return this;
    }

    public Player setLastTakeSnowball(LocalDateTime time) {
        this.lastTakeSnowball = time;
        return this;
    }

    public void save(DataCluster cluster) {
        cluster.getPlayerRepository().save(this);
    }

    @Override
    public int compareTo(@NotNull Player o) {
        return (this.score < o.getScore()) ? 1 : -1;
    }
}