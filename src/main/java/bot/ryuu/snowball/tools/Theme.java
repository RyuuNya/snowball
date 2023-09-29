package bot.ryuu.snowball.tools;

import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public class Theme {
    public static Color MAIN = new Color(111, 150, 250);
    public static Color WAIT = new Color(255, 155, 54);
    public static Color ERROR = new Color(255, 54, 94);

    public static EmbedBuilder main() {
        return new EmbedBuilder().setColor(MAIN);
    }

    public static EmbedBuilder await() {
        return new EmbedBuilder().setColor(WAIT);
    }

    public static EmbedBuilder error() {
        return new EmbedBuilder().setColor(ERROR);
    }
}