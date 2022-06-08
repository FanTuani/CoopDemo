package lqw.coop.Game;

import lqw.coop.Coop;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class Basics implements Listener {
    private final Coop plugin = Coop.instance;

    public Basics() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
    }

    @EventHandler
    public void onPlayerGetExp(PlayerExpChangeEvent event) {
        event.setAmount(0);
    }

    @EventHandler
    public void onPlayerHungry(FoodLevelChangeEvent event) {
        event.getEntity().setFoodLevel(20);
        event.setCancelled(true);
    }

}
