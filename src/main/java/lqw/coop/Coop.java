package lqw.coop;

import lqw.coop.Game.Basics;
import lqw.coop.Guns.Gun;
import lqw.coop.Guns.TestGun;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.plugin.java.JavaPlugin;

public final class Coop extends JavaPlugin {
    public static Coop instance;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("CoopDemo loaded !!!!!!!!!!!!!!!!!!!!!!");
        new Basics();
//        new WallJump();
        new Gun();
        new TestGun();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
