package bot.ryuu.snowball.game.random;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Rarity {
    USUAL("Usual"),
    RARE("Rare"),
    LEGENDARY("Legendary");

    private final String name;
}