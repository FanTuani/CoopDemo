package lqw.coop.Features;

import lqw.coop.Coop;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Gun implements Listener {
    private final Coop plugin = Coop.instance;

    public Gun() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerShoot(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getInventory().getItemInMainHand().getType() == Material.WOODEN_HOE) {
            player.sendMessage("Shoot!");
            Vector vector = player.getLocation().getDirection();
            vector.multiply(-2);
            new BukkitRunnable() {
                final Location parLoc = player.getEyeLocation();

                @Override
                public void run() {
                    parLoc.subtract(vector);
                    player.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, parLoc,
                            1, 0, 0, 0, 0);
                    if (parLoc.getWorld().getNearbyEntities(parLoc, 1.5, 1.5, 1.5).size() != 0) {
                        for (Entity entity : parLoc.getWorld().getNearbyEntities(parLoc, 0.5, 0.5, 0.5)) {
                            if (entity instanceof Damageable) {
                                ((Damageable) entity).damage(1);
                            }
                        }
                    }
                    if (parLoc.distance(player.getLocation()) > 200 || parLoc.getBlock().getType() != Material.AIR)
                    cancel();
                }
            }.runTaskTimer(plugin, 0, 1);
        }
    }
}
