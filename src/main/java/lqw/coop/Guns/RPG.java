package lqw.coop.Guns;

import lqw.coop.Game.Game;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Collection;

public class RPG extends AbstractGun implements Listener {

    public RPG() {
        this.gunItemType = Material.DIAMOND_SHOVEL;
        AbstractGun.guns.add(Material.DIAMOND_SHOVEL);
        this.bulletSpeed = 2;
        this.maxRange = 200;
        this.coolDownTicks = 5;
        this.reloadTicks = 100;
        this.damage = 4F;
        this.capacity = 2;
        this.hitBox = 4;
        this.particle = Particle.FIREWORKS_SPARK;
        this.shootSound = Sound.ENTITY_WITHER_SHOOT;
        this.recoil = 1F;
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
                            continue;
                        }
                        player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);
                        for (Entity target : nearbyEntities) {
                            if (target instanceof Player)
                                player.sendMessage(ChatColor.YELLOW + target.getName() + "'s HP: " + Math.round(((Player) target).getHealth()));
                        }
                        player.getWorld().createExplosion(parLoc, (float) damage, false, false);
                        cancel(); // 命中实体判定
                        break;
                    }
                    lastLoc = parLoc.clone();
                    vector.multiply(1.02); // 速度逐渐加快
                    parLoc.subtract(vector); // 位移
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    @EventHandler
    public void onExplode(BlockExplodeEvent event) {
        event.setYield(0);
    }
}
