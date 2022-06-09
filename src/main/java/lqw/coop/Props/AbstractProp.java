package lqw.coop.Props;

import lqw.coop.Coop;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;

public abstract class AbstractProp implements Listener {
    protected final Coop plugin = Coop.instance;

    protected Material propItemType;

    protected AbstractProp() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    abstract void magic(Player player);

    @EventHandler
    public void onPlayerInt(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        PlayerInventory inv = player.getInventory();
        if (inv.getItemInMainHand().getType() == propItemType) {
            event.setCancelled(true);
            if (event.getHand() == EquipmentSlot.HAND) {
                int propIndex = inv.first(propItemType);
                if (inv.getItem(propIndex).getAmount() == 1) {
                    inv.clear(propIndex);
                } else {
                    inv.getItem(propIndex).setAmount(inv.getItem(propIndex).getAmount() - 1);
                }
                Bukkit.broadcastMessage(player.getName() + "使用了一个道具: " + propItemType.name());
                magic(player);
            }
        }
    }

}
