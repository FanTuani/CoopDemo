package lqw.coop.Props;

import lqw.coop.Game.Game;
import lqw.coop.Utils.SendingActionBarMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Glass extends AbstractProp implements Listener {

    public Glass() {
        this.propItemType = Material.GLASS;
        Game.props.add(Material.GLASS);
    }

    @Override
    void magic(Player player) {
        int duration = 3;
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20 * duration, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * duration, 1));
        Game.sendTitle2All("", ChatColor.YELLOW + player.getName() + " 隐身了！", 2, 20, 2);
        new BukkitRunnable() {
            int time = duration+1;

            @Override
            public void run() {
                if (time-- == 0) cancel();
                else new SendingActionBarMessage(player, ChatColor.YELLOW + String.valueOf(time), 1).start(plugin);
            }
        }.runTaskTimer(plugin, 0, 20);
    }
}
