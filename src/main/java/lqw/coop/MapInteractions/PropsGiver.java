package lqw.coop.MapInteractions;

import lqw.coop.Coop;
import lqw.coop.Game.Game;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PropsGiver implements Listener {
    private final Coop plugin = Coop.instance;
    public static ArrayList<Location> goldBlocks = new ArrayList<>(); // 防 reload 丢金块

    public PropsGiver() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerStandOnGoldBlock(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location blockLoc = player.getLocation();
        blockLoc.setY(blockLoc.getY() - 1);
        if (blockLoc.getBlock().getType() == Material.GOLD_BLOCK) {
            player.sendTitle("", ChatColor.BLUE + "Prop got!", 2, 10, 2);
            player.playSound(blockLoc, Sound.ENTITY_ITEM_PICKUP, 1, 1);
            List list = Arrays.asList(Game.props.toArray());
            player.getInventory().addItem(new ItemStack((Material) list.get(new Random().nextInt(list.size()))));
           Game.delayRecoverBlocks(blockLoc, Material.GOLD_BLOCK, Material.BONE_BLOCK, 20*30);
            new BukkitRunnable() {
                int times = 0;
                Location parLoc = blockLoc.clone();
                @Override
                public void run() {
                    if (times++ == 30) cancel();
                    parLoc.getWorld().spawnParticle(Particle.HEART, parLoc, 1);
                    parLoc.setY(parLoc.getY()+2);
                }
            }.runTaskTimer(plugin, 1,1);
        }
    }


}
