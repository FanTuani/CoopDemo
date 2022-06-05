package lqw.coop.Guns;

import lqw.coop.Coop;
import lqw.coop.Game.Game;
import org.bukkit.Location;
import org.bukkit.Material;
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
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;

public abstract class AbstractGun implements Listener {
    protected final Coop plugin = Coop.instance;
    protected Material gunItemType;
    protected int bulletSpeed, maxRange, coolDownTicks, reloadTicks, damage, capacity;
    protected double hitBox;

    protected AbstractGun() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    abstract void shoot(PlayerEvent event);

    private void beforeShoot(PlayerEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (!Game.getIsReloading(player) && Game.getIsCooledOver(player) && Game.reduceCapacity(item, capacity)) {
            // 若 不在换弹 且 射速允许 且 (弹药量-1 若弹药量!=0) 则开火
            Game.setIsCooledOver(player, false);
            new BukkitRunnable() {
                @Override
                public void run() {
                    Game.setIsCooledOver(player, true);
                }
            }.runTaskLater(plugin, coolDownTicks);
            shoot(event);
        } else if (!Game.getIsReloading(player) && Game.getIsCooledOver(player)) {
            reload(event);
        }
    }

    private void afterShoot(PlayerEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        int maxDur = item.getType().getMaxDurability();
        if (Game.getDurability(item) < maxDur / capacity) reload(event); // 空弹夹自动换弹
    }

    private void reload(PlayerEvent event) {
        Player player = event.getPlayer();
        int maxDur = player.getInventory().getItemInMainHand().getType().getMaxDurability();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (maxDur == Game.getDurability(item)) return;
        Game.setIsReloading(player, true);
        new BukkitRunnable() {
            int nowDur = 0;

            @Override
            public void run() {
                if (nowDur >= maxDur) {
                    Game.setIsReloading(player, false);
                    cancel();
                }
                if (player.getInventory().getItemInMainHand().getType() == gunItemType) {
                    Game.setDurability(item, nowDur + maxDur / reloadTicks);
                    nowDur = Game.getDurability(item);
                    player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, 0.1F, 2F);
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    protected boolean checkAndDamageNearByEntities(Player shooter, Location loc, int damage) {
        Collection<Entity> collection = loc.getWorld().getNearbyEntities(loc, hitBox, hitBox, hitBox);
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
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) { // 右键尝试射击
                // 射击前后
                beforeShoot(event);
                afterShoot(event);
                event.setCancelled(true);

            } else if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) { // 左键换弹
                if (!Game.getIsReloading(event.getPlayer())) // 防止打断换弹
                    reload(event);
            }
        }
    }

    @EventHandler
    public void onPlayerIntEnt(PlayerInteractAtEntityEvent event) {
        if (event.getPlayer().getInventory().getItemInMainHand().getType() == gunItemType) {
            beforeShoot(event);
            afterShoot(event);
            event.setCancelled(true);
        }
    }
}
