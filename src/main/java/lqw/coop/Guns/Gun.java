package lqw.coop.Guns;

import lqw.coop.Coop;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Collection;

public class Gun implements Listener {
    private final Coop plugin = Coop.instance;
    private final Material GunItemType = Material.WOODEN_HOE; // 木锄
    private final int bulletSpeed = 5, maxRange = 200;

    public Gun() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerInt(PlayerInteractEvent event) {
        if (event.getPlayer().getInventory().getItemInMainHand().getType() == GunItemType) {
            shoot(event);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerIntEnt(PlayerInteractAtEntityEvent event) {
        if (event.getPlayer().getInventory().getItemInMainHand().getType() == GunItemType) {
            shoot(event);
            event.setCancelled(true);
        }
    }

    private void shoot(PlayerEvent event) {
        Player player = event.getPlayer();
        Vector vector = player.getLocation().getDirection();
        vector.multiply(-1);
        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1, 1);

        new BukkitRunnable() {
            final Location parLoc = player.getEyeLocation();
            int times = 0;

            @Override
            public void run() {
                for (int i = 1; i <= bulletSpeed; i++) {
                    if (++times % 3 == 0) // 弹道稀疏
                        player.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, parLoc,
                                1, 0, 0, 0, 0);
                    if (checkAndDamageNearByEntities(player, parLoc)) { // 命中判定
                        cancel();
                        player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);
                        break;
                    }
                    parLoc.subtract(vector); // 位移
                }
                if (parLoc.distance(player.getLocation()) > maxRange || parLoc.getBlock().getType() != Material.AIR)
                    cancel(); // 消失判定
            }
        }.runTaskTimer(plugin, 0, 1);
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
