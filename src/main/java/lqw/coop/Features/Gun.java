package lqw.coop.Features;

import lqw.coop.Coop;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Collection;

public class Gun implements Listener {
    private final Coop plugin = Coop.instance;

    public Gun() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerShoot(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getInventory().getItemInMainHand().getType() == Material.WOODEN_HOE) {
            Vector vector = player.getLocation().getDirection();
            vector.multiply(-1);
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1, 1);

            new BukkitRunnable() {
                final Location parLoc = player.getEyeLocation();
                final int speed = 5;

                @Override
                public void run() {
                    for (int i = 1; i <= speed; i++) {
                        player.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, parLoc,
                                1, 0, 0, 0, 0);
                        if (checkAndDamageNearByEntities(player, parLoc)) {
                            cancel();
                            player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);
                            break;
                        }
                        parLoc.subtract(vector);
                    }
                    if (parLoc.distance(player.getLocation()) > 500 || parLoc.getBlock().getType() != Material.AIR)
                        cancel();
                }
            }.runTaskTimer(plugin, 0, 1);
        }
    }

    @EventHandler
    public void onPlayerRightClick(PlayerInteractEvent event) {
        if (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.WOODEN_HOE) {
            event.setCancelled(true);
        }
    }

    private boolean checkAndDamageNearByEntities(Player shooter, Location loc) {
        Collection<Entity> collection = loc.getWorld().getNearbyEntities(loc, 0.5, 0.5, 0.5);
        if (collection.size() == 0) return false;
        else {
            for (Entity entity : collection) {
                if (entity == shooter) return false;
                if (entity instanceof LivingEntity && !entity.isDead()) {
                    LivingEntity target = (LivingEntity) entity;
                    target.setNoDamageTicks(1);
                    target.damage(1);
                    shooter.sendMessage(String.valueOf(Math.round(target.getHealth())));
                    break;
                }
            }
            return true;
        }
    }
}
