package lqw.coop.Props;

import lqw.coop.Coop;
import lqw.coop.Game.Game;
import org.bukkit.*;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class CreeperHead extends AbstractProp implements Listener {
    public CreeperHead() {
        this.propItemType = Material.CREEPER_HEAD;
        this.propName = "手榴弹";
        Game.props.add(Material.CREEPER_HEAD);
    }

    @Override
    void magic(Player player) {
        Vector vector = player.getLocation().getDirection();
        vector.setY(vector.getY() + 0.5);
        Creeper creeper = (Creeper) player.getWorld().spawnEntity(player.getLocation().add(vector), EntityType.CREEPER);
        creeper.setVelocity(player.getLocation().getDirection().multiply(2.5));
        if (Math.random() < 0.3) {
            creeper.setPowered(true);
        }
        creeper.ignite();
        creeper.setExplosionRadius(4);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (creeper.isDead()) cancel();
                else {
                    creeper.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, creeper.getEyeLocation(), 1, 0, 0, 0, 0);
                    creeper.setFallDistance(0);
                }
            }
        }.runTaskTimer(plugin, 0, 1);
        new BukkitRunnable() {
            @Override
            public void run() {
                int box = creeper.isPowered() ? 10 : 5;
                if (creeper.isPowered()){
                    creeper.getWorld().spawnParticle(Particle.CAMPFIRE_SIGNAL_SMOKE, creeper.getLocation(), 100, 1, 1, 1, 3);
                    creeper.getWorld().strikeLightningEffect(creeper.getLocation());
                }
                for (Entity target : creeper.getNearbyEntities(box, box, box)) {
                    if (target instanceof Player) {
                        Player targetPlayer = (Player) target;
                        Vector kb = targetPlayer.getLocation().toVector().subtract(creeper.getLocation().toVector());
                        kb.normalize();
                        kb.setY(0.4);
                        double val = Math.min(box, target.getLocation().distance(creeper.getLocation()));
                        kb.multiply(box - val).multiply(0.5);
                        targetPlayer.setVelocity(targetPlayer.getVelocity().add(kb)); // 击退
                    }
                }
            }
        }.runTaskLater(Coop.instance, 30);
    }

    @EventHandler
    public void onCreeperExplode(EntityExplodeEvent event) {
        event.setCancelled(true);
        Entity entity = event.getEntity();
        entity.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, entity.getLocation(), 1);
        entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 2.5F, 1);
    }
}
