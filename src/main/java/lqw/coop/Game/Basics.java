package lqw.coop.Game;

import lqw.coop.Coop;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Basics implements Listener {
    private final Coop plugin = Coop.instance;

    public Basics() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Game.setIsCooledOver(event.getPlayer(), true);
    }
}
