package lqw.coop;

import lqw.coop.Features.WallJump;
import lqw.coop.Game.Basics;
import lqw.coop.Game.Game;
import lqw.coop.Guns.AbstractGun;
import lqw.coop.Guns.RPG;
import lqw.coop.Guns.Rifle;
import lqw.coop.Guns.Snipe;
import lqw.coop.Props.Beacon;
import lqw.coop.Props.BlackDye;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class Coop extends JavaPlugin {
    public static Coop instance;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("CoopDemo loaded !!!!!!!!!!!!!!!!!!!!!!");
        new Basics();
        new WallJump();
        new Rifle();
        new Snipe();
        new RPG();

        new Beacon();
        new BlackDye();

        for (Player player : getServer().getOnlinePlayers()) {
            player.setLevel(0);
            player.setExp(0);
            player.sendTitle(ChatColor.GREEN + "插件重载完成!", "Yee", 5, 10, 10);
            ItemStack item = player.getInventory().getItemInMainHand();
            if (AbstractGun.guns.contains(item.getType())) {
                Game.setDurability(item, item.getType().getMaxDurability());
            }
        }
    }

    @Override
    public void onDisable() {
        for (Player player : getServer().getOnlinePlayers()) {
            player.sendTitle(ChatColor.RED + "正在重载插件...", "如果你正在换弹，拿着枪别动！", 2, 40, 20);
        }
        // Plugin shutdown logic
    }
}
