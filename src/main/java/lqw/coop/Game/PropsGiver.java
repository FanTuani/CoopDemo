package lqw.coop.Game;

import lqw.coop.Coop;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PropsGiver implements Listener {
    private final Coop plugin = Coop.instance;

    public PropsGiver() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerStandOnGoldBlock(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location blockLoc = player.getLocation();
        blockLoc.setY(blockLoc.getY() - 1);
        if (blockLoc.getBlock().getType() == Material.GOLD_BLOCK) {
            player.sendTitle("", ChatColor.BLUE + "wow!", 2, 10, 2);
            player.playSound(blockLoc, Sound.ENTITY_ITEM_PICKUP, 1, 1);
            List list = Arrays.asList(Game.props.toArray());
            player.getInventory().addItem(new ItemStack((Material) list.get(new Random().nextInt(list.size()))));
            blockLoc.getBlock().setType(Material.BONE_BLOCK);
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (blockLoc.getBlock().getType() == Material.BONE_BLOCK) { // 防某些lry用
                        blockLoc.getBlock().setType(Material.GOLD_BLOCK);
                        blockLoc.getWorld().playSound(blockLoc, Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
                    }
                }
            }.runTaskLater(plugin, 600);
        }
    }
}
