package lqw.coop.Props;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Beacon extends AbstractProp implements Listener {
    public Beacon() {
        this.propItemType = Material.BEACON; // 信标
    }

    @Override
    void magic(Player player) {
        for (Player otherPlayer : plugin.getServer().getOnlinePlayers()) {
            if (otherPlayer != player) {
                otherPlayer.sendTitle("", ChatColor.RED + "你现形了！", 2, 100, 2);
                otherPlayer.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 100, 1));
                otherPlayer.setVelocity(otherPlayer.getVelocity().setY(0.8));
                otherPlayer.getWorld().playSound(otherPlayer.getLocation(), Sound.AMBIENT_UNDERWATER_EXIT, 0.8F, 2);
            }
        }
    }
}
