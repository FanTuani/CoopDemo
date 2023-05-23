package lqw.coop.Weapons.Guns;

import lqw.coop.Game.Game;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;

public class RPG extends AbstractGun implements Listener {

    public RPG() {
        this.gunItemType = Material.DIAMOND_SHOVEL;
        this.bulletSpeed = 2;
        this.maxRange = 300;
        this.coolDownTicks = 20 * 2;
        this.reloadTicks = 20 * 3;
        this.damage = 4F;
        this.capacity = 1;
        this.hitBox = 3;
        this.particle = Particle.FIREWORKS_SPARK;
        this.shootSound = Sound.ENTITY_WITHER_SHOOT;
        this.recoil = 1.3F;
        initGunItem();
    }

    @Override
    protected void initGunItem() {
        ItemStack item = new ItemStack(gunItemType, 1);
        item.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("RPG");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "伤害: 爆炸");
        lore.add(ChatColor.GRAY + "弹夹容量: " + capacity);
        lore.add(ChatColor.GRAY + "弹道速度: " + bulletSpeed + "++");
        meta.setLore(lore);
        item.setItemMeta(meta);
        AbstractGun.guns.add(item);
    }

    @Override
    protected void shoot(Player player) {
        Vector vector = player.getLocation().getDirection();
        vector.multiply(-0.5);
        player.getWorld().playSound(player.getLocation(), shootSound, 1, 5F);

        new BukkitRunnable() {
            final Location parLoc = player.getEyeLocation();
            Location lastLoc = parLoc.clone();

            @Override
            public void run() {
                for (int i = 1; i <= bulletSpeed; i++) {
                    Collection<Entity> nearbyEntities = parLoc.getWorld().getNearbyEntities(parLoc, hitBox, hitBox, hitBox);
                    player.getWorld().spawnParticle(particle, parLoc, 1, 0, 0, 0, 0.1F);
                    player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, parLoc, 1, 0, 0, 0, 1F);

                    if (parLoc.distance(player.getLocation()) > maxRange || !Game.isPermeable(parLoc.getBlock().getType())) {
                        player.getWorld().createExplosion(lastLoc, (float) damage, false, false);
                        cancel(); // 命中方块判定
                        break;
                    }
                    if (!nearbyEntities.isEmpty()) { // 命中其他玩家
                        if (nearbyEntities.size() == 1 && nearbyEntities.contains(player)) { // 防止自伤
                            lastLoc = parLoc.clone();
                            parLoc.subtract(vector);
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    if (player.getGameMode() == GameMode.SPECTATOR)
                                        Game.sendTitle2All(ChatColor.YELLOW + player.getName() + " 自爆了！",
                                                "我们赶到现场时连尸体都没发现！", 2, 20, 2);
                                }
                            }.runTaskLater(plugin, 2);
                            continue;
                        }
                        player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);
                        for (Entity target : nearbyEntities) {
                            if (target instanceof Player) {
                                Player targetPlayer = (Player) target;
                                Vector kb = targetPlayer.getLocation().toVector().subtract(parLoc.toVector());
                                kb.normalize();
                                kb.setY(0.4);
                                targetPlayer.setVelocity(targetPlayer.getVelocity().add(kb.multiply(2))); // 击退
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        if (targetPlayer.getGameMode() == GameMode.SPECTATOR && target != player)
                                            Game.sendTitle2All(ChatColor.YELLOW + target.getName() + " 爆炸了！",
                                                    "投弹手是 " + player.getName(), 2, 20, 2);
                                        else player.sendMessage(ChatColor.YELLOW + target.getName()
                                                + "'s HP: " + Math.round(targetPlayer.getHealth()));
                                    }
                                }.runTask(plugin);
                            }
                        }
                        player.getWorld().createExplosion(parLoc, (float) damage, false, false);
                        cancel(); // 命中实体判定
                        break;
                    }
                    lastLoc = parLoc.clone();
                    vector.multiply(1.03); // 速度逐渐加快
                    parLoc.subtract(vector); // 位移
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    @EventHandler
    public void onExplode(BlockExplodeEvent event) {
        event.setYield(0);
    }

    @EventHandler
    public void slowMotion(PlayerMoveEvent event) {
        if (event.getPlayer().getInventory().getItemInMainHand().getItemMeta() != null)
            if (event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase("RPG")) {
                event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5, 1));
            }
    }
}
