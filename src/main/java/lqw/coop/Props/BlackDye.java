package lqw.coop.Props;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

public class BlackDye extends AbstractProp implements Listener {

    public BlackDye() {
        this.propItemType = Material.BLACK_DYE;
    }

    @Override
    void magic(Player player) {
        player.sendTitle("", ChatColor.GREEN + "Disturbance sent!", 2, 16, 2);
        for (Player otherPlayer : plugin.getServer().getOnlinePlayers()) {
            if (otherPlayer != player) {
                otherPlayer.getWorld().playSound(otherPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1F, 2);
                otherPlayer.sendTitle("", ChatColor.YELLOW + "Disturbance is coming soon...", 2, 16, 2);
                new BukkitRunnable() {
                    int ticks = 0;

                    @Override
                    public void run() {
                        if (ticks++ == 60) cancel();
                        Random random = new Random();
                        Vector vector = new Vector(0.5 - random.nextFloat(), 0.5 - random.nextFloat(), 0.5 - random.nextFloat());
                        otherPlayer.setVelocity(vector);
                        otherPlayer.getWorld().playSound(otherPlayer.getLocation(), Sound.ITEM_BOOK_PUT, 0.2F, 2);
                    }
                }.runTaskTimer(plugin, 20, 1);
            }
        }
    }
}
