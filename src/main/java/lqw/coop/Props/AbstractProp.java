package lqw.coop.Props;

import lqw.coop.Coop;
import lqw.coop.Utils.SendingActionBarMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.PlayerInventory;

public abstract class AbstractProp implements Listener {
    protected final Coop plugin = Coop.instance;

    protected Material propItemType;
    protected String propName;

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
                new SendingActionBarMessage(ChatColor.YELLOW + player.getName() +
                        " 使用了 " + propName, 1).start(plugin);
                magic(player);
            }
        }
    }

}
