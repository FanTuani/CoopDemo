package lqw.coop.Features;

import lqw.coop.Coop;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class WallJump implements Listener {
    private final Coop plugin = Coop.instance;

    public WallJump() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerTryWallJump(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        if (!player.isFlying() && player.getAllowFlight() && player.getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
            player.setAllowFlight(false);
            Vector oriVec = player.getLocation().getDirection();
            Vector vector = new Vector(oriVec.getX() * 0.8, 0.5, oriVec.getZ() * 0.8);
            player.setVelocity(vector);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.isOnGround())
            player.setAllowFlight(true);
    }
//
//    private boolean checkNearWall(Player player) {
//        if (!player.getAllowFlight() && player.isOnGround()) {
//            Location location = player.getLocation();
//            int[][] cl = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
//            for (int i = 0; i < 4; i++) {
//                location.setX(location.getX() + cl[i][0]);
//                location.setZ(location.getZ() + cl[i][1]);
//                Block block = location.getBlock();
//                if (block.getType() != Material.AIR) {
//                    block.setType(Material.JUNGLE_WOOD);
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
}
