package bot.ryuu.snowball.game;

import bot.ryuu.snowball.game.power.Power;
import bot.ryuu.snowball.player.Player;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public interface PowerSystem {
    HashMap<String, Power> activeObject = new HashMap<>();

    static void putActiveObject(Player player, Power power) {
        if (player.containObject(power))
            activeObject.put(player.getId(), power);
    }

    static void removeActiveObject(Player player) {
        activeObject.remove(player.getId());
    }

    static Power getActiveObject(Player player) {
        return activeObject.get(player.getId());
    }

    static boolean isActiveObject(Player player) {
        return getActiveObject(player) != null;
    }
}
