package lqw.coop;

import lqw.coop.Game.Basics;
import lqw.coop.Game.Game;
import lqw.coop.Guns.AbstractGun;
import lqw.coop.Guns.RPG;
import lqw.coop.Guns.Rifle;
import lqw.coop.Guns.Snipe;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class Coop extends JavaPlugin {
    public static Coop instance;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("CoopDemo loaded !!!!!!!!!!!!!!!!!!!!!!");
        new Basics();
//        new WallJump();
        new Rifle();
        new Snipe();
        new RPG();
        for (Player player : getServer().getOnlinePlayers()) {
            player.setLevel(0);
            player.setExp(0);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
