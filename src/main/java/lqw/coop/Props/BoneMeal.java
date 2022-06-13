package lqw.coop.Props;

import lqw.coop.Game.Game;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class BoneMeal extends AbstractProp implements Listener {

    public BoneMeal() {
        this.propItemType = Material.BONE_MEAL;
        Game.props.add(Material.BONE_MEAL);
    }

    @Override
    void magic(Player player) {
        Location location = player.getEyeLocation();
        player.getWorld().spawnParticle(Particle.CAMPFIRE_SIGNAL_SMOKE, location, 50, 1, 1, 1, 0.005);
        new BukkitRunnable() {
            int dur = 20 * 10;

            @Override
            public void run() {
                if (dur-- == 0) cancel();
                int val = (int) Math.sqrt(dur + 10);
                player.getWorld().spawnParticle(Particle.CAMPFIRE_SIGNAL_SMOKE, location, val, 1.5, 1.25, 1.5, 0.005);
            }
        }.runTaskTimer(plugin, 0, 1);
    }
}
