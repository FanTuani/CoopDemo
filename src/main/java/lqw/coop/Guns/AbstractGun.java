package lqw.coop.Guns;

import lqw.coop.Coop;
import lqw.coop.Game.Game;
import lqw.coop.Utils.SendingActionBarMessage;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public abstract class AbstractGun implements Listener {
    protected final Coop plugin = Coop.instance;
    protected Material gunItemType;
    protected Particle particle;
    protected Sound shootSound;
    protected int bulletSpeed, maxRange, coolDownTicks, reloadTicks, capacity;
    protected double hitBox, damage, recoil;

    public static final HashSet<Material> guns = new HashSet<>();

    protected HashMap<UUID, Integer> whoCap = new HashMap<>();

    protected AbstractGun() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    protected void shoot(Player player) {
        Vector vector = player.getLocation().getDirection();
        vector.multiply(-0.5);
        player.getWorld().playSound(player.getLocation(), shootSound, 1, 5F);

        new BukkitRunnable() {
            final Location parLoc = player.getEyeLocation();
            int times = 0;

            @Override
            public void run() {
                for (int i = 1; i <= bulletSpeed; i++) {
                    if (++times % 5 == 0) // 弹道粒子效果稀疏
                        player.getWorld().spawnParticle(particle, parLoc, 1, 0, 0, 0, 0);
                    if (parLoc.distance(player.getLocation()) > maxRange || parLoc.getBlock().getType() != Material.AIR) {
                        cancel(); // 消失判定
                        break;
                    }
                    if (checkAndDamageNearByEntities(player, parLoc)) {
                        cancel(); // 命中判定
                        player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);
                        break;
                    }
                    parLoc.subtract(vector); // 位移
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    private void beforeShoot(Player player) {
        if (!Game.getIsReloading(player) && Game.getIsCooledOver(player) && player.getLevel() != 0) {
            // 若 不在换弹 且 射速允许 且 (弹药量-1 若弹药量!=0) 则开火
            player.setLevel(player.getLevel() - 1);
            shoot(player);
            afterShoot(player);
        }
    }

    private void afterShoot(Player player) {
        Vector vector = player.getLocation().getDirection();
        vector.multiply(-recoil);
        if (!player.isSneaking()) // 潜行无后座
            vector.add(player.getVelocity());
        player.setVelocity(vector);
        if (player.getLevel() == 0) {
            reload(player); // 空弹夹自动换弹
        } else {
            player.setExp(0);
            intoCD(player, coolDownTicks);
        }
    }

    protected void reload(Player player) {
        if (!Game.getIsReloading(player) && player.getLevel() < capacity) {

            new SendingActionBarMessage(player, "Reloading...", 1).start(plugin);

            player.setLevel(0);
            player.setExp(0);
            int maxDur = player.getInventory().getItemInMainHand().getType().getMaxDurability();

            new BukkitRunnable() {
                int nowDur = 0, tick = 0;
                ItemStack item, itemLast;

                @Override
                public void run() {
                    itemLast = item;
                    item = player.getInventory().getItemInMainHand();
                    if (item.getType() == gunItemType) {
                        tick++;
                        Game.setDurability(item, nowDur + maxDur / reloadTicks);
                        nowDur = Game.getDurability(item);
                        player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, 0.1F, 2F);
                    }
//                    else {
//                        tick = 0;
//                        nowDur = 0;
//                        Game.setDurability(itemLast, 0);
//                    }
                    if (tick == reloadTicks) {
                        Game.setDurability(item, maxDur);
                        player.setLevel(capacity);
                        player.setExp(1F);
                        whoCap.put(player.getUniqueId(), capacity);
                        cancel();
                    }
                }
            }.runTaskTimer(plugin, 0, 1);

        }
    }

    protected boolean checkAndDamageNearByEntities(Player shooter, Location loc) {
        Collection<Entity> collection = loc.getWorld().getNearbyEntities(loc, hitBox, hitBox, hitBox);
        if (collection.size() == 0) return false;
        else {
            for (Entity entity : collection) {
                if (entity == shooter) return false;
                if (entity instanceof LivingEntity && !entity.isDead()) {
                    LivingEntity target = (LivingEntity) entity;
                    target.damage(damage, shooter);
                    target.setNoDamageTicks(1);
                    shooter.sendMessage(ChatColor.YELLOW + target.getName() + "'s HP: " + Math.round(target.getHealth()));
                    break;
                }
            }
            return true;
        }
    }

    @EventHandler
    public void onPlayerInt(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getInventory().getItemInMainHand().getType() == gunItemType) {
            event.setCancelled(true);
            if (event.getHand() == EquipmentSlot.HAND) {
                if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) { // 右键尝试射击
                    beforeShoot(player);
                }
//                else if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) { // 左键换弹
//                    if (!Game.getIsReloading(event.getPlayer())) // 防止打断换弹
//                        reload(player);
//                } // 已被切换主副手换弹取代
            }
        }
    }

    @EventHandler
    public void onPlayerReload(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        event.setCancelled(true);
        if (player.getInventory().getItemInMainHand().getType() == gunItemType) {
            if (!Game.getIsReloading(event.getPlayer())) // 防止打断换弹
                reload(player);
        }
    }

    private void intoCD(Player player, int time) {
        player.setExp(0);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.getInventory().getItemInMainHand().getType() == gunItemType) {
                    if (player.getExp() + 1F / coolDownTicks <= 1F) {
                        player.setExp((player.getExp() + 1F / time));
                    } else {
                        player.setExp(1F);
                        cancel();
                    }
                } else cancel();
                if (Game.getIsReloading(player)) {
                    player.setLevel(0);
                    player.setExp(0);
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }


    @EventHandler
    public void onPlayerIntEnt(PlayerInteractAtEntityEvent event) {
        if (event.getPlayer().getInventory().getItemInMainHand().getType() == gunItemType
                && event.getHand() == EquipmentSlot.HAND) {
            beforeShoot(event.getPlayer());
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerSwitchGun(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        ItemStack newItem = event.getPlayer().getInventory().getItem(event.getNewSlot());
        ItemStack preItem = event.getPlayer().getInventory().getItem(event.getPreviousSlot());
//        try {
//            newItem = event.getPlayer().getInventory().getItem(event.getNewSlot());
//        } catch (NullPointerException e) {
//            newItem = null;
//        }
//        try {
//            preItem = event.getPlayer().getInventory().getItem(event.getPreviousSlot());
//        } catch (NullPointerException e) {
//            preItem = null;
//        }
        if (preItem != null && preItem.getType() == gunItemType) { // for pre gun class
            whoCap.put(uuid, player.getLevel());
        }
        if (newItem != null && newItem.getType() == gunItemType) { // for new gun class
            if (!whoCap.containsKey(uuid)) { // first get this gun
                whoCap.put(uuid, capacity);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.setLevel(capacity);
                    }
                }.runTask(plugin);
            } else {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.setLevel(whoCap.get(uuid));
                    }
                }.runTask(plugin);
            }
            intoCD(player, coolDownTicks);
        }
        if ((newItem == null || !guns.contains(newItem.getType())))
            if (preItem != null && guns.contains(preItem.getType())) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.setLevel(0);
                        player.setExp(0);
                    }
                }.runTask(plugin);
            }
    }
}
