package bot.ryuu.snowball.theme;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.dv8tion.jda.api.entities.emoji.CustomEmoji;
import net.dv8tion.jda.api.entities.emoji.Emoji;

@Getter
@AllArgsConstructor
public enum ThemeEmoji {
    TIMER(Emoji.fromCustom("snowball", 1150081195347026043L, false)),
    ERROR(Emoji.fromCustom("error", 1150081277119180871L, false)),
    SNOWBALL(Emoji.fromCustom("snowball", 1150081248270762086L, false)),
    THROW(Emoji.fromCustom("throw", 1150081185410715689L, false)),
    POWER(Emoji.fromCustom("power", 1150081258693607535L, false)),
    KING(Emoji.fromCustom("king", 1150081267845570611L, false));

    private final CustomEmoji emoji;
}
