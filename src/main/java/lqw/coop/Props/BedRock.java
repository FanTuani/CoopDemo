package lqw.coop.Props;

import lqw.coop.Game.Game;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class BedRock extends AbstractProp implements Listener {
    public BedRock() {
        this.propItemType = Material.BEDROCK;
        this.propName = "伟哥";
        Game.props.add(Material.BEDROCK);
    }

    @Override
    void magic(Player player) {
        Game.sendTitle2All("", ChatColor.YELLOW + player.getName() + " 硬起来了！", 2, 20, 2);
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 10, 1));
        if (player.getPotionEffect(PotionEffectType.SPEED) == null)
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 10, 0));
        else
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 10, player.getPotionEffect(PotionEffectType.SPEED).getAmplifier() + 1));
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANCIENT_DEBRIS_FALL, 0.5F, 2);

        new BukkitRunnable() {
            double R = 2;

            @Override
            public void run() {
                if (player.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE) == null) cancel();
                new BukkitRunnable() {
                    int degree = 0;

                    @Override
                    public void run() {
                        if (degree > 360 || player.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE) == null)
                            cancel();
                        else {
                            Location location = player.getLocation();
                            degree += 10;
                            for (int i = 1; i <= 3; i++) {
                                double rad = Math.toRadians(degree + i * 120);
                                double x = Math.cos(rad) * R;
                                double z = Math.sin(rad) * R;
                                player.getWorld().spawnParticle(Particle.DRIP_LAVA, location.clone().add(x, 1, z),
                                        1, 0, 0, 0, 1);
                                player.getWorld().spawnParticle(Particle.DRIP_LAVA, location.clone().add(-x, 1, -z),
                                        1, 0, 0, 0, 1);
                            }
                        }
                    }
                }.runTaskTimer(plugin, 0, 1);
            }
        }.runTaskTimer(plugin, 0, 360 / 10);
    }
}
