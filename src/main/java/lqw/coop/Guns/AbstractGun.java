package lqw.coop.Guns;

import lqw.coop.Coop;
import lqw.coop.Game.Game;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Collection;

public abstract class AbstractGun implements Listener {
    protected final Coop plugin = Coop.instance;
    protected Material gunItemType;
    protected int bulletSpeed, maxRange, coolDownTicks, damage, capacity;

    protected AbstractGun() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    abstract void shoot(PlayerEvent event);

    private void reload(PlayerEvent event) {
        Player player = event.getPlayer();
        if (!Game.getIsReloading(player)) {
            Game.setIsReloading(player, true);
            int maxDur = player.getInventory().getItemInMainHand().getType().getMaxDurability();
            ItemStack item = player.getInventory().getItemInMainHand();
            new BukkitRunnable() {
                @Override
                public void run() {
                    int nowDur = Game.getDurability(item);
                    if (nowDur >= maxDur) {
                        Game.setIsReloading(player, false);
                        cancel();
                    }
                    if (player.getInventory().getItemInMainHand().getType() == gunItemType) {
                        Game.setDurability(item, nowDur + maxDur / capacity);
                        player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, 0.1F, 2F);
                    }
                }
            }.runTaskTimer(plugin, 0, 2);
        }
    }

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
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getPlayer().getInventory().getItemInMainHand().getType() == gunItemType) {
                shoot(event);
                event.setCancelled(true);
            }
        } else {
            reload(event);
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
