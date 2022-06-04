package lqw.coop;

import lqw.coop.Game.Basics;
import lqw.coop.Game.Game;
import lqw.coop.Guns.Rifle;
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
        Game.initMaps();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
