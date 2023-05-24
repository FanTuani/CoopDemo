package lqw.coop.Props;

import lqw.coop.Game.Game;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Firework extends AbstractProp implements Listener {
    public Firework() {
        this.propItemType = Material.FIREWORK_ROCKET;
        this.propName = "弹射器";
        Game.props.add(Material.FIREWORK_ROCKET);
    }

    @Override
    void magic(Player player) {
        Vector vector = player.getLocation().getDirection();
        vector.setY(vector.getY() + 0.5);
        player.setVelocity(vector.multiply(1.75));
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 0.25F, 2);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.isOnGround()) cancel();
                player.setFallDistance(0);
                player.getWorld().spawnParticle(Particle.LAVA, player.getLocation(), 1);

            }
        }.runTaskTimer(plugin, 2, 1);
    }
}
