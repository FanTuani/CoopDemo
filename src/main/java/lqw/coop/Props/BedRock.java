package lqw.coop.Props;

import lqw.coop.Game.Game;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BedRock extends AbstractProp implements Listener {
    public BedRock() {
        this.propItemType = Material.BEDROCK;
        Game.props.add(Material.BEDROCK);
    }

    @Override
    void magic(Player player) {
        Game.sendTitle2All("", ChatColor.YELLOW + player.getName() + " 硬起来了！", 2, 20, 2);
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 10, 1));
    }

}
