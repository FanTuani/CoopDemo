package lqw.coop.Game;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CoolDownRunnable extends BukkitRunnable {
    private final Player player;

    public CoolDownRunnable(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        Game.setIsCooledOver(player, true);
    }
}
