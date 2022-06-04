package lqw.coop.Game;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class Game {
    private static HashMap<UUID, Boolean> isCooledOver = new HashMap<>();

    public static boolean getIsCooledOver(Player player) {
        UUID uuid = player.getUniqueId();
        return isCooledOver.getOrDefault(uuid, false);
    }

    public static void setIsCooledOver(Player player, boolean isCooled) {
        isCooledOver.put(player.getUniqueId(), isCooled);
    }
}
