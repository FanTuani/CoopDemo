package lqw.coop.Guns;

import lqw.coop.Game.CoolDownRunnable;
import lqw.coop.Game.Game;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Rifle extends AbstractGun implements Listener {

    public Rifle() {
        this.gunItemType = Material.STONE_HOE;
        this.bulletSpeed = 5;
        this.maxRange = 200;
        this.coolDownTicks = 4;
        this.damage = 5;
        this.capacity = 10;
    }

    @Override
    void shoot(PlayerEvent event) {
        Player player = event.getPlayer();
        if (!Game.getIsReloading(player) && Game.reduceCapacity(player.getInventory().getItemInMainHand(), capacity))
            // 弹药量-1 若弹药量=0则阻止开火 若在换弹则阻止开火
            if (Game.getIsCooledOver(player)) {
                Vector vector = player.getLocation().getDirection();
                vector.multiply(-1);
                player.playSound(player.getLocation(), Sound.BLOCK_BAMBOO_BREAK, 1, 5F);

                new BukkitRunnable() {
                    final Location parLoc = player.getEyeLocation();
                    int times = 0;

                    @Override
                    public void run() {
                        for (int i = 1; i <= bulletSpeed; i++) {
                            if (++times % 3 == 0) // 弹道稀疏
                                player.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, parLoc,
                                        1, 0, 0, 0, 0);
                            if (checkAndDamageNearByEntities(player, parLoc, damage)) { // 命中判定
                                cancel();
                                player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);
                                break;
                            }
                            if (parLoc.distance(player.getLocation()) > maxRange || parLoc.getBlock().getType() != Material.AIR)
                                cancel(); // 消失判定
                            parLoc.subtract(vector); // 位移
                        }
                    }
                }.runTaskTimer(plugin, 0, 1);
                Game.setIsCooledOver(player, false);
                new CoolDownRunnable(player).runTaskLater(plugin, coolDownTicks);
            }
    }

}
