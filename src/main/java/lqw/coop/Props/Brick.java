package lqw.coop.Props;

import lqw.coop.Game.Game;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class Brick extends AbstractProp implements Listener {
    public Brick() {
        this.propItemType = Material.BRICK;
        Game.props.add(Material.BRICK);
    }

    @Override
    void magic(Player player) {
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 0.2F, 2);
        Location location = player.getLocation();
        double R = 4;

        new BukkitRunnable() {
            int time = 20 * 10;

            @Override
            public void run() {
                if (time <= 0) cancel();
                time -= 5;

                for (Entity entity : location.getWorld().getNearbyEntities(location, R, R, R)) {
                    if (entity instanceof Player) {
                        Player p = (Player) entity;
                        if (p.getHealth() <= 19) {
                            p.setHealth(p.getHealth() + 1);
                            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.3F, 0.1F);
                        }
                    }
                }
                if (time > 40)
                    for (int degree = 0; degree < 360; degree += 10) {
                        double rad = Math.toRadians(degree);
                        double x = Math.cos(rad) * R;
                        double z = Math.sin(rad) * R;
                        player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, location.clone().add(x, 0.25, z),
                                1, 0, 0, 0, 0);
                    }
            }
        }.runTaskTimer(plugin, 0, 5);
        new BukkitRunnable() {
            int hight = 1;
            Location loc = location.clone();

            @Override
            public void run() {
                for (int degree = 0; degree < 360; degree += 10) {
                    double rad = Math.toRadians(degree);
                    double x = Math.cos(rad) * R;
                    double z = Math.sin(rad) * R;
                    player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, loc.clone().add(x, 0.25, z),
                            1, 0, 0, 0, 0);
                }
                loc.setY(loc.getY() + hight++);
                if (hight > 60) cancel();
            }
        }.runTaskTimer(plugin, 0, 1);
    }
}
