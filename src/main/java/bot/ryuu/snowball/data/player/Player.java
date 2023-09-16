package bot.ryuu.snowball.data.player;

import bot.ryuu.snowball.game.power.Power;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Set;

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

    private int score;
    private int snowball;

    private LocalDateTime lastTakeSnowball;
    private LocalDateTime lastRandomPower;

    private Set<Power> powers;
    private Power active;

    public Player incSnowball(int increment) {
        this.snowball += increment;
        return this;
    }

    public Player incScore(int increment) {
        this.score += increment;
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Player addPower(Power power) {
        this.powers.add(power);
        return this;
    }

    public Player removePower(Power power) {
        this.powers.remove(power);
        return this;
    }

    public boolean isPower(Power power) {
        return this.powers.contains(power);
    }

    public Player addActive(Power power) {
        this.active = power;
        return this;
    }

    public Player removeActive(Power power) {
        this.active = power;
        this.powers.remove(power);
        return this;
    }

    public boolean isActive() {
        return this.active != null;
    }

    public Player activatePower(Power power) {
        if (this.powers.contains(power))
            this.active = power;
        return this;
    }

    public Player setLastTakeSnowball(LocalDateTime lastTakeSnowball) {
        this.lastTakeSnowball = lastTakeSnowball;
        return this;
    }

    public Player setLastRandomPower(LocalDateTime lastRandomPower) {
        this.lastRandomPower = lastRandomPower;
        return this;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setSnowball(int snowball) {
        this.snowball = snowball;
    }

    public void setPowers(Set<Power> powers) {
        this.powers = powers;
    }

    public void setActive(Power active) {
        this.active = active;
    }

    public Power getActive() {
        return active;
    }

    public void save(PlayerRepository playerRepository) {
        playerRepository.save(this);
    }

    @Override
    public int compareTo(@NotNull Player o) {
        return (this.score < o.getScore()) ? 1 : -1;
    }
}
