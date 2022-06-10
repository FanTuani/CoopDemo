package lqw.coop.Game;

import lqw.coop.Coop;
import org.bukkit.*;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Random;

public class RandomRespawn implements Listener {
    private final Coop plugin = Coop.instance;

    public RandomRespawn() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerDie(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player && event.getFinalDamage() >= ((Player) event.getEntity()).getHealth()) {
            event.setCancelled(true);
            Player player = (Player) event.getEntity();
            player.setHealth(20);
            player.sendTitle(ChatColor.RED + "寄", "", 2, 21, 2);
            player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 0.1F);
            player.setGameMode(GameMode.SPECTATOR);
            Vector vector = player.getVelocity();
            vector.setY(vector.getY() + 3);
            player.setVelocity(vector);
            for (Player otherPlayer : plugin.getServer().getOnlinePlayers()) {
                otherPlayer.sendTitle(ChatColor.YELLOW+player.getName()+" 寄了！","",2, 10,2);
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.setGameMode(GameMode.SURVIVAL);
                    Configuration config = plugin.getConfig();
                    List list = config.getList("1");
                    int x1 = new Integer(list.get(0).toString());
                    int x2 = new Integer(list.get(1).toString());
                    list = config.getList("2");
                    int z1 = new Integer(list.get(0).toString());
                    int z2 = new Integer(list.get(1).toString());
                    Location loc = player.getLocation();
                    int r1 = new Random().nextInt(Math.abs(x1 - z1)) + x1;
                    int r2 = new Random().nextInt(Math.abs(x2 - z2)) + x2;
                    loc.setX(r1);
                    loc.setZ(r2);
                    int y = 60;
                    while (new Location(player.getWorld(), r1, y, r2).getBlock().getType() == Material.AIR) {
                        y--;
                    }
                    loc.setY(y + 3);
                    player.teleport(loc);
                }
            }.runTaskLater(plugin, 25);
        }
    }
}
