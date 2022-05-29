package lqw.coop.Features;

import lqw.coop.Coop;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.util.Vector;

public class WallJump implements Listener {
    private final Coop plugin = Coop.instance;

    public WallJump() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerTryWallJump(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        if (!player.isFlying()) {
            event.setCancelled(true);
            Vector oriVec = player.getLocation().getDirection();
            Vector vector = new Vector(oriVec.getX(), 0.5, oriVec.getZ());
            player.setVelocity(vector);
        }
    }
}
