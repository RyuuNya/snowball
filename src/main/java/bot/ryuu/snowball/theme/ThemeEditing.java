package bot.ryuu.snowball.theme;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

public class ThemeEditing {

    @Getter
    @AllArgsConstructor
    private enum Editing {
        ITALIC("*"), BOLD("**"), SPOILER("||");

        private final String pattern;
    }

    public static String italic(Object obj) {
        return Editing.ITALIC.getPattern() + obj + Editing.ITALIC.getPattern();
    }

    public static String bold(Object obj) {
        return Editing.BOLD.getPattern() + obj + Editing.BOLD.getPattern();
    }

    public static String spoiler(Object obj) {
        return Editing.SPOILER.getPattern() + obj + Editing.SPOILER.getPattern();
    }
}
