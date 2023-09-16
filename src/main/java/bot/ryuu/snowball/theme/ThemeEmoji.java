package bot.ryuu.snowball.theme;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.dv8tion.jda.api.entities.emoji.CustomEmoji;
import net.dv8tion.jda.api.entities.emoji.Emoji;

@Getter
@AllArgsConstructor
public enum ThemeEmoji {
    TIMER(Emoji.fromCustom("snowball", 1150462955029270548L, false)),
    ERROR(Emoji.fromCustom("error", 1150462971663892541L, false)),
    SNOWBALL(Emoji.fromCustom("snowball", 1150457071741960274L, false)),
    THROW(Emoji.fromCustom("throw", 1150457084975005706L, false)),
    POWER(Emoji.fromCustom("power", 1150081258693607535L, false)),
    KING(Emoji.fromCustom("king", 1150459472964231168L, false));

    private final CustomEmoji emoji;
}
