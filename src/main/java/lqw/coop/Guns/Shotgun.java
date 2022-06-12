package lqw.coop.Guns;

import lqw.coop.Game.Game;
import lqw.coop.Utils.SendingActionBarMessage;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Collection;

public class Shotgun extends AbstractGun implements Listener {
    public Shotgun() {
        this.gunItemType = Material.IRON_HOE;
        AbstractGun.guns.add(Material.IRON_HOE);
        this.bulletSpeed = 30;
        this.maxRange = 10;
        this.coolDownTicks = 5;
        this.reloadTicks = 100;
        this.damage = 4F;
        this.capacity = 2;
        this.hitBox = 0.25;
        this.particle = Particle.CLOUD;
        this.shootSound = Sound.ENTITY_DRAGON_FIREBALL_EXPLODE;
        this.recoil = 0.5F;
    }

    @Override
    protected void shoot(Player player) {
        for (int i = 1; i <= 6; i++) {
            Vector vector = player.getLocation().getDirection();
            vector.setX(vector.getX() + (Math.random() - 0.5) * 0.5);
            vector.setY(vector.getY() + (Math.random() - 0.5) * 0.5);
            vector.setZ(vector.getZ() + (Math.random() - 0.5) * 0.5);
            vector.multiply(-0.5);
            player.getWorld().playSound(player.getLocation(), shootSound, 0.75F, 1F);
            new BukkitRunnable() {
                final Location parLoc = player.getEyeLocation();
                int times = 0;

                @Override
                public void run() {
                    for (int i = 1; i <= bulletSpeed; i++) {
                        if (++times % 3 == 0) // 弹道粒子效果稀疏
                            player.getWorld().spawnParticle(particle, parLoc, 1, 0, 0, 0, 0);
                        if (parLoc.distance(player.getLocation()) > maxRange || !Game.isPermeable(parLoc.getBlock().getType())) {
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
    }

    @Override
    protected boolean checkAndDamageNearByEntities(Player shooter, Location loc) {
        Collection<Entity> collection = loc.getWorld().getNearbyEntities(loc, hitBox, hitBox, hitBox);
        if (collection.size() == 0) return false;
        else {
            for (Entity entity : collection) {
                if (entity == shooter || ((Player) entity).getGameMode() == GameMode.SPECTATOR) return false;
                if (entity instanceof LivingEntity && !entity.isDead()) {
                    LivingEntity target = (LivingEntity) entity;
                    target.damage(damage);
                    target.setVelocity(target.getVelocity().add(target.getLocation().subtract(shooter.getLocation()).toVector().normalize().multiply(knockBack)));
                    target.setNoDamageTicks(0);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (((Player) target).getGameMode() == GameMode.SPECTATOR) {
                                Game.sendTitle2All(ChatColor.YELLOW + target.getName() + " 被秒杀！",
                                        "凶手是 " + shooter.getName(), 2, 20, 2);
                                Game.scoredPlus(shooter);
                            } else
                                new SendingActionBarMessage(shooter, ChatColor.YELLOW + target.getName() + "'s HP: " + Math.round(target.getHealth()), 1).start(plugin);
                        }
                    }.runTask(plugin);
                    break;
                }
            }
            return true;
        }
    }
}
