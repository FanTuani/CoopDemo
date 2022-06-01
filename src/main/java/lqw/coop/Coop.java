package lqw.coop;

import lqw.coop.Features.Gun;
import lqw.coop.Features.WallJump;
import org.bukkit.plugin.java.JavaPlugin;

public final class Coop extends JavaPlugin {
    public static Coop instance;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("CoopDemo loaded !!!!!!!!!!!!!!!!!!!!!!");
//        new WallJump();
        new Gun();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
