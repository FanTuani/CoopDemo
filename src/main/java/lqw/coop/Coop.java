package lqw.coop;

import org.bukkit.plugin.java.JavaPlugin;

public final class Coop extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("CoopDemo loaded !!!!!!!!!!!!!!!!!!!!!!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
