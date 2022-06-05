package lqw.coop.Guns;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Rifle extends AbstractGun implements Listener {

    public Rifle() {
        this.gunItemType = Material.STONE_HOE;
        this.bulletSpeed = 20;
        this.maxRange = 200;
        this.coolDownTicks = 3;
        this.reloadTicks = 30;
        this.damage = 2;
        this.capacity = 25;
        this.hitBox = 0.25;
    }

    @Override
    void shoot(PlayerEvent event) {
        Player player = event.getPlayer();
        Vector vector = player.getLocation().getDirection();
        vector.multiply(-0.5);
        player.playSound(player.getLocation(), Sound.BLOCK_BAMBOO_BREAK, 1, 5F);

        new BukkitRunnable() {
            final Location parLoc = player.getEyeLocation();
            int times = 0;

            @Override
            public void run() {
                for (int i = 1; i <= bulletSpeed; i++) {
                    if (++times % 5 == 0) // 弹道粒子效果稀疏
                        player.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, parLoc,
                                1, 0, 0, 0, 0);
                    if (parLoc.distance(player.getLocation()) > maxRange || parLoc.getBlock().getType() != Material.AIR) {
                        cancel(); // 消失判定
                        break;
                    }
                    if (checkAndDamageNearByEntities(player, parLoc, damage)) {
                        cancel(); // 命中判定
                        player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);
                        break;
                    }
                    parLoc.subtract(vector); // 位移
                }
            }
        }.runTaskTimer(plugin, 0, 1);

    }

}
