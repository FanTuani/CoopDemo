package lqw.coop.Guns;

import lqw.coop.Coop;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Collection;

public abstract class AbstractGun implements Listener {
    protected final Coop plugin = Coop.instance;
    protected Material gunItemType;
    protected int bulletSpeed, maxRange, coolDownTicks, damage;

    protected AbstractGun() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    abstract void shoot(PlayerEvent event);

    protected boolean checkAndDamageNearByEntities(Player shooter, Location loc, int damage) {
        Collection<Entity> collection = loc.getWorld().getNearbyEntities(loc, 0.5, 0.5, 0.5);
        if (collection.size() == 0) return false;
        else {
            for (Entity entity : collection) {
                if (entity == shooter) return false;
                if (entity instanceof LivingEntity && !entity.isDead()) {
                    LivingEntity target = (LivingEntity) entity;
                    target.setNoDamageTicks(1);
                    target.damage(damage);
                    shooter.sendMessage(String.valueOf(Math.round(target.getHealth())));
                    break;
                }
            }
            return true;
        }
    }

    @EventHandler
    public void onPlayerInt(PlayerInteractEvent event) {
        if (event.getPlayer().getInventory().getItemInMainHand().getType() == gunItemType) {
            shoot(event);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerIntEnt(PlayerInteractAtEntityEvent event) {
        if (event.getPlayer().getInventory().getItemInMainHand().getType() == gunItemType) {
            shoot(event);
            event.setCancelled(true);
        }
    }
}
